echo on

@cd %~dp0
@call cd ../Frontend
call npm install

cd %~dp0
pause