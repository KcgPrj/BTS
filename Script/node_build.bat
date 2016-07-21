echo on

@cd %~dp0
@call cd ../UiServer/src/main/resources/src
call npm build

cd %~dp0
pause