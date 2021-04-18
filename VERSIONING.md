# Versioning System

## The format: `M.m.r`

### `M`

This placeholder contains the **major** version of the plugin.

This number is only updated when the plugin undergoes a very significant change in code, e.g., a re-code of the entire
resource.

### `m`

This placeholder contains the **minor** version of the plugin.

This number is updated whenever a new update comes out, that is not a revision of the previous update - e.g. adding in 5
new Actions.

### `r`

This placeholder contains the **revision** version of the plugin.

If a change or bugfix needs to be made to the previous update, then the revision number will be incremented.

## Additional Information

* Every subsequent build of the plugin **must** increment **one** of the values in the version format, but **only if**
  any changes were made. If the same code is being re-compiled, the version of such code should remain the same.

***

`VERSIONING.md` `v1`