# FileFixer from Team A

## Written by Arvin Shertukde, Ciaran Diep, Divya Desai, and Roman Mazzoni

## Application Overview

Just create the JSON converter. Start with command line version, and layer more UI on later.
Stretch goals: Any UI at all, ability to have dynamic schema, schema defined on a per-thing
basis in config files. UI can show the object JSON, and store object in DB too.

## User Stories:

As a user I should be able to

1. Choose a file layout
2. Choose a fixed file to convert
3. View the file’s JSON representation

## How to Use

FileFixer allows the user to upload a fixed length file alongside choosing the layout of the supplied file, which is limited to at most **2 MB** in size. Once the necessary files are uploaded, the file can be converted to JSON. The first 3 JSON objects are displayed on the page, but the entire JSON file can be downloaded to the user's device if need be.

The inputted fixed length file can also contain multiple objects, which FileFixer will be able to handle as long as the proper length is maintained for each entry. By default, there should be **no delimiter** between each fixed length entry.

The ```Person``` file layout contains the following fields: 
1. First name
2. Last name
3. Date of birth
4. Email
5. Phone number
6. Citizen Status

, which adheres to the project guidelines.

In the event that one or more files are malformed or incompatible, then an error will be displayed on the page in red text. 

### Custom File Specifications

#### General Syntax

If the ```Custom``` file layout is chosen, then a supplementary JSON file is required which specifies the start and end indices of every field. This file needs to be structured as follows:

```
{
    "field_name_1": {
        "startpos": <index of first character>
        "endpos": <index of last character>
    },
    "field_name_2": {
        "startpos": <index of first character>
        "endpos": <index of last character>
    },
    ...
}
```

The field names can be any valid JavaScript string (aside from ```delimiter```), but the strings ```startpos``` and ```endpos``` are specific attribute names and must be **exactly** correct.

Each field can also contain another optional attribute for the desired data type, with the ```datatype``` attribute:

```
"field_name": {
    "startpos": ...
    "endpos": ...
    "datatype": <"STRING", "BOOLEAN", "INTEGER", or "NUMBER">
},
```

If the datatype is not specified, then FileFixer will interpret the value as a string by default. For Boolean specifically, only string values of ```T```, ```Y```, or ```1``` will evaluate to **True**, anything else will evaluate to **False**. For Integers and Numbers, the given values will try to be interpreted as Integers and Doubles respectively, according to core Java's implementation. 

Moreover, order of each field is preserved from the specification file, so in the JSON output for the above specification, ```field_name_1``` will always appear first and above ```field_name_2```, for example.

#### Custom Delimiter

If the given file contains multiple objects which are delimited by a certain substring, then a delimiter string or RegEx expression can also be specified with the ```delimiter``` attribute:

```
{
    ...
    "field_name_n": {
        "startpos": ...
        "endpos": ...
    },
    "delimiter: <delimiter substring>
}
```

Because the delimiter field is defined on the same tier as the field names, the name delimiter is a reserved keyword and **cannot** be used as a field name.

