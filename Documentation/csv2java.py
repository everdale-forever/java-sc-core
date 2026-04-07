import csv
import re
import os
import argparse

def to_camel_case(name):
    """Converts a spaced string like 'String Col' to 'stringCol'"""
    # Remove quotes if present
    name = name.strip('"').strip()
    parts = re.split(r'[\s_-]+', name)
    # Handle empty strings from trailing spaces
    parts = [p for p in parts if p]
    if not parts:
        return ""
    return parts[0].lower() + ''.join(word.capitalize() for word in parts[1:])

def to_pascal_case(name):
    """Converts filename like 'example' to 'Example'"""
    return name.capitalize()

def get_base_type(csv_type):
    """Extracts base type, handling 'Number' -> 'int' and 'array' suffixes"""
    t = csv_type.lower().strip().replace("array", "").strip()
    if t == 'number':
        return 'int'
    return t

def get_java_type(base_type, is_array):
    """Returns the correct Java type (primitive for single, wrapper for Lists)"""
    types_map = {
        'string': 'String',
        'int': 'Integer',
        'boolean': 'Boolean'
    }
    wrapper = types_map.get(base_type, base_type)
    
    if is_array:
        return wrapper
    else:
        # Use primitives for non-array types
        if base_type == 'int':
            return 'int'
        if base_type == 'boolean':
            return 'boolean'
        return wrapper

def get_method_prefix(base_type):
    """Returns the prefix for getter methods (String, Int, Bool)"""
    if base_type == 'int':
        return 'Int'
    if base_type == 'boolean':
        return 'Bool'
    return 'String'

def process_csv(filepath):
    filename = os.path.splitext(os.path.basename(filepath))[0]
    class_name = f"Logic{to_pascal_case(filename)}Data"

    with open(filepath, 'r', encoding='utf-8') as f:
        reader = csv.reader(f)
        rows = list(reader)

    if len(rows) < 2:
        raise ValueError("CSV must have at least a header row and a type row")

    headers = rows[0]
    types = rows[1]
    data_rows = rows[2:]

    fields = []
    ref_assignments = []
    array_ref_assignments = []
    required_base_types = set()

    for i, (header, csv_type) in enumerate(zip(headers, types)):
        header = header.strip()
        csv_type = csv_type.strip().lower()
        
        if not header or not csv_type:
            continue

        is_explicit_array = 'array' in csv_type
        is_implicit_array = False
        
        # Detect implicit array: "if it has data, and the first column is empty"
        if not is_explicit_array and data_rows:
            for row in data_rows:
                # Ensure row is long enough to check
                if len(row) > i and row[0].strip() == "" and row[i].strip() != "":
                    is_implicit_array = True
                    break

        is_array = is_explicit_array or is_implicit_array
        base_type = get_base_type(csv_type)
        required_base_types.add(base_type)

        java_type = get_java_type(base_type, is_array)
        field_name = to_camel_case(header)
        method_prefix = get_method_prefix(base_type)

        # Generate Field
        if is_array:
            fields.append(f"    public List<{java_type}> {field_name} = new ArrayList<>();")
            array_ref_assignments.append(
                f'            this.{field_name}.add(this.get{method_prefix}ArrayValue("{header}", i));'
            )
        else:
            fields.append(f"    public {java_type} {field_name};")
            ref_assignments.append(
                f'        this.{field_name} = this.get{method_prefix}Value("{header}");'
            )

    # Build Java File
    java_code = f"""package com.banaanae.javasccore.logic.data.TablesData;

import com.banaanae.javasccore.logic.data.LogicData;
import com.banaanae.javasccore.logic.data.LogicDataTable;
import com.banaanae.javasccore.titan.csv.CSVRow;
import java.util.ArrayList;
import java.util.List;

public class {class_name} extends LogicData {{
{chr(10).join(fields)}
    
    public {class_name}(CSVRow row, LogicDataTable table) {{
        super(row, table);
    }}
    
    @Override
    public void createReferences() {{
        super.createReferences();
        final int arrSize = row.getBiggestArraySize();
{chr(10).join(ref_assignments)}
        for (int i = 0; i < arrSize; i++) {{
{chr(10).join(array_ref_assignments)}
        }}
    }}
"""

    # Generate only the required getter methods
    if 'string' in required_base_types:
        java_code += """
    public String getStringValue(String name) {
        return (String) this.getValue(name, 0);
    }
    
    public String getStringArrayValue(String name, int index) {
        return (String) this.getValue(name, index);
    }
"""
    if 'int' in required_base_types:
        java_code += """
    public int getIntValue(String name) {
        return this.getIntegerValue(name, 0);
    }
    
    public int getIntArrayValue(String name, int index) {
        return this.getIntegerValue(name, index);
    }
"""
    if 'boolean' in required_base_types:
        java_code += """
    public boolean getBoolValue(String name) {
        return this.getBooleanValue(name, 0);
    }
    
    public boolean getBoolArrayValue(String name, int index) {
        return this.getBooleanValue(name, index);
    }
"""

    java_code += "}\n"
    
    # Clean up empty lines if no arrays or no singles existed
    java_code = re.sub(r'\n{3,}', '\n\n', java_code)
    
    return class_name, java_code

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Convert specific CSV format to Java LogicData class.")
    parser.add_argument("csv_file", help="Path to the input CSV file")
    args = parser.parse_args()

    class_name, java_output = process_csv(args.csv_file)
    
    out_file = f"{class_name}.java"
    with open(out_file, "w", encoding="utf-8") as f:
        f.write(java_output)
        
    print(f"Successfully generated {out_file}")