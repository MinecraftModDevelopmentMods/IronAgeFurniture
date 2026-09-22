# Versioning

IronAgeFurniture uses `product-version.target-suffix` as four numeric parts.

For this branch, `0.3.0.112021` means product release `0.3.0`, Minecraft
`1.12.2`, and loader code `1` for Forge. The runtime mod ID remains
`ironagefurniture`; the Maven coordinate is
`zone.moddev.mc:iron-age-furniture:0.3.0.112021`.

Release tags must match the complete four-part version exactly. The generic
default-branch dispatcher maps suffix `112021` to `master-1.12` and refuses a
tag whose target suffix, branch metadata, or built artifacts disagree.
