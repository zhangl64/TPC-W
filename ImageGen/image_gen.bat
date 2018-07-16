@echo off
setlocal

set /a NUM_IMAGES=100

set /a PERCENT=%NUM_IMAGES%/100

set /a A1=1
set /a A2=%PERCENT%*45

set /a B1=%A2%+1
set /a B2=%PERCENT%*80

set /a C1=%B2%+1
set /a C2=%PERCENT%*95

set /a D1=%C2%+1
set /a D2=%PERCENT%*99

set /a E1=%D2%+1
set /a E2=%PERCENT%*100

FOR /L %%i IN (%A1% 1 %A2%) DO (
call last.exe 5 item%%i.jpg
)

FOR /L %%i IN (%B1% 1 %B2%) DO (
call last.exe 10 item%%i.jpg
)

FOR /L %%i IN (%C1% 1 %C2%) DO (
call last.exe 50 item%%i.jpg
)

FOR /L %%i IN (%D1% 1 %D2%) DO (
call last.exe 100 item%%i.jpg
)

FOR /L %%i IN (%E1% 1 %E2%) DO (
call last.exe 250 item%%i.jpg
)

FOR /L %%i IN (1 1 %NUM_IMAGES%) DO (
call last.exe 5 thumb%%i.jpg
)

:end