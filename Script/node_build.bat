echo on

@cd %~dp0
@call cd ../Frontend
call npm run build

cd %~dp0
pause