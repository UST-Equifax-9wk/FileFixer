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

In the event that one or more files are malformed or incompatible, then an error will be displayed on the page in red text. 

### Person File Layout

The two Person file layouts both specify 6 different fields in the following order:
1. First name - string
2. Last name - string
3. Date of birth - string
4. Email - string
5. Phone number - string
6. US Citizen Status - boolean

For the ```Person``` option, the fields adhere to the following ranges of indices, inclusive. Note that the given indices are **1-indexed, instead of 0-indexed**:
1. First name - 1-15, 15 chars
2. Last name - 16-30, 15 chars
3. Date of birth - 31-38, 8 chars
4. Email - 39-68, 30 chars
5. Phone number - 69-78, 10 chars
6. US Citizen Status - 79-79, 1 char

For the ```Person with Long Data``` option, the same fields adhere to the following ranges instead:
1. First name - 1-20, 20 chars
2. Last name - 21-40, 20 chars
3. Date of birth - 41-48, 8 chars
4. Email - 49-78, 30 chars
5. Phone number - 79-88, 10 chars
6. US Citizen Status - 89-89, 1 char

### Car File Layouts

Mostly created just as a proof of concept to show that multiple presets can be made, the two Car file layouts specify 6 different fields:
1. Manufacturer - string
2. Model - string
3. Color - string
4. US state code - string
5. License plate number - string
6. Year - string

The ```Car``` option adheres to the following ranges:
1. Manufacturer - 1-15, 15 chars
2. Model - 16-35, 20 chars
3. Color - 36-45, 10 chars
4. US state code - 46-47, 2 chars
5. License plate number - 48-55, 8 chars
6. Year - 56-59, 4 chars

The ```Car with Long Data``` option adheres to the following ranges:
1. Manufacturer - 1-20, 20 chars
2. Model - 21-45, 25 chars
3. Color - 46-60, 15 chars
4. US state code - 61-62, 2 chars
5. License plate number - 63-70, 8 chars
6. Year - 71-74, 4 chars

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

The positions of each character are based on a **1-indexed system**, so FileFixer will return an error if any of the given positions are less than 1. An error will also occur if any of the given start positions are greater than the corresponding end position, which would result in a backward range.

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

