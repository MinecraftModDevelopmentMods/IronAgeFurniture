param([Parameter(Mandatory=$true)][string]$LegacyLanguageDirectory)
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
$languageRoot = Join-Path $projectRoot 'src\main\resources\assets\ironagefurniture\lang'
$bankRoot = Join-Path $projectRoot 'gradle\locales'
$english = Get-Content -LiteralPath (Join-Path $languageRoot 'en_us.json') -Raw | ConvertFrom-Json -AsHashtable

function Read-Language([string]$Path) {
    $result = @{}
    foreach ($line in Get-Content -LiteralPath $Path -Encoding utf8) {
        if ($line.StartsWith('#') -or -not $line.Contains('=')) { continue }
        $parts = $line.Split('=',2)
        $result[$parts[0]] = $parts[1]
    }
    return $result
}
function Legacy-Key([string]$Key) {
    if (-not $Key.StartsWith('block.ironagefurniture.')) { return $Key }
    $key = $Key.Replace('block.ironagefurniture.', 'tile.ironagefurniture.').Replace('dark_oak','big_oak')
    if ($key -match '^(tile\.ironagefurniture\.chair_wood_ironage_bench(?:_back)?_padded)_(black|blue|brown|cyan|gray|green|light_blue|light_gray|lime|magenta|orange|pink|purple|red|white|yellow)_(single|left|middle|right)_(.+)$') {
        $suffix = if ($Matches[2] -eq 'red') { '' } else { '.'+$Matches[2] }
        return "$($Matches[1])_$($Matches[3])_$($Matches[4])$suffix.name"
    }
    return "$key.name"
}
$oldEnglish = Read-Language (Join-Path $LegacyLanguageDirectory 'en_us.lang')
$byLabel = @{}
foreach ($key in ($oldEnglish.Keys | Sort-Object)) {
    if (-not $byLabel.ContainsKey($oldEnglish[$key])) { $byLabel[$oldEnglish[$key]] = $key }
}
New-Item -ItemType Directory -Path $bankRoot -Force | Out-Null
foreach ($locale in @('de_au','de_de','en_ca','en_pt','es_es','es_mx','fr_ca','fr_fr','ja_jp','ko_kr','pt_br','pt_pt','ru_ru','zh_cn')) {
    $legacy = Read-Language (Join-Path $LegacyLanguageDirectory "$locale.lang")
    $translated = [ordered]@{}
    foreach ($key in ($english.Keys | Sort-Object)) {
        $oldKey = Legacy-Key $key
        if ($key -eq 'itemGroup.ironagefurniture') { $oldKey = 'itemGroup.IronAgeFurniture' }
        if (-not $legacy.ContainsKey($oldKey) -and $byLabel.ContainsKey($english[$key])) { $oldKey=$byLabel[$english[$key]] }
        # Keep only actual translations. Missing or formerly English-only
        # labels follow the current English catalog instead of freezing it.
        if ($legacy.ContainsKey($oldKey) -and $legacy[$oldKey] -ne $oldEnglish[$oldKey]) { $translated[$key]=$legacy[$oldKey] }
    }
    $text = ($translated | ConvertTo-Json -Depth 4) + "`n"
    [System.IO.File]::WriteAllText((Join-Path $bankRoot "$locale.json"), $text.Replace("`r`n","`n"), [System.Text.UTF8Encoding]::new($false))
    Write-Output "$locale : $($translated.Count) retained translated labels"
}
