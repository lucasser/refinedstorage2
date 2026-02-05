## Format

Stored as CompoundTag

### Grid:

```json
{
  "item_dict": {},
  "workstations": {}
}
```

### Item_dict element:

item is an itemstack with count of 1 to easily save it as CompoundTag

hashvalue is ItemStack.hashItemAndComponents(itemStack)

```json
{
  "hashvalue": {
    "item": "ItemStack (use item.saveOptional())",
    "amount": "int"
  }
}
```

This structure will allow to look up entries by hash number. And looking up by itemstack will never be required because you can always run it through the hasher

### Workstation:

idvalue is recipe id. ex: minecraft:crafting

can query for specific workstation

result should only be used for polymorph. (stonecutting type recipes are basically polymorph).
Recipe inputs should always be parsed on world load/tab change and the result checked against the possible outputs

Special workstations (like anvil) should be able to add more params (like string).

```json
{
  "idvalue": {
    "width": "int",
    "height": "int",
    "slots": {
      "number": {
        "hash": "int",
        "amount": "int"
      }
    },
    "result": "itemStack"
  }
}
```

## Functionality:

Look up item by hash, thru item_dict

get all items in grid, thru item_dict

## Recipe types

### vanilla

- crafting
    - 3x3 grid
    - result
- smithing
    - 1x3 grid
    - result
- stonecutting
    - 1x1 grid
    - result
- anvil
    - 1x2 grid
    - string
    - xp cost?
    - result
- grindstone
    - 2x1 grid
    - result

### Modded

- extended crafting
    - KxK grid
    - int for polymorph?
    - result
- rechiseled
    - not relevant?
    - 1x1 grid
    - int?
    - result
