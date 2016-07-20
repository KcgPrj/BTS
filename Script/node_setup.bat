echo on

@cd %~dp0
@call cd ../UiServer/src/main/resources/src
call npm install

cd %~dp0
pause