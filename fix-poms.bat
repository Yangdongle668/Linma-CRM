@echo off
setlocal enabledelayedexpansion

for /d %%D in (crm-modules\module-*) do (
    echo Processing %%D\pom.xml...
    set "file=%%D\pom.xml"
    
    REM Read file and remove common-* dependencies
    powershell -Command ^
        "$content = Get-Content '!file!' -Raw; ^
         $content = $content -replace '(?s)\s*<!-- Common \w+ -->.*?<dependency>.*?<groupId>com\.crm</groupId>.*?<artifactId>common-\w+</artifactId>.*?</dependency>', ''; ^
         $content | Set-Content '!file!' -NoNewline"
    
    echo Done: %%D
)

echo All modules processed!
pause
