@echo off

cd /D "%~dp0"

set PATH=%PATH%;%SystemRoot%\system32
set CALL_COMMAND=%~1
set PROMPT_FILE=%~2
set OUTPUT_FILENAME=%~3
set OUTPUT_DIR=%~4
set HISTORY_PROMPT=%~5


echo "%CD%"| findstr /C:" " >nul && echo This script relies on Miniconda which can not be silently installed under a path with spaces. && goto end

@rem fix failed install when installing to a separate drive
set TMP=%cd%\installer_files
set TEMP=%cd%\installer_files

@rem config
set CONDA_ROOT_PREFIX=%cd%\installer_files\conda
set INSTALL_ENV_DIR=%cd%\installer_files\env

@rem environment isolation
set PYTHONNOUSERSITE=1
set PYTHONPATH=
set PYTHONHOME=
set "CUDA_PATH=%INSTALL_ENV_DIR%"
set "CUDA_HOME=%CUDA_PATH%"

echo ----- BARK INFINITY COMMAND LINE------
echo Type 'python bark_perform.py' to run the CLI.
echo Type 'python bark_perform.py --help' for options
echo Type 'conda deactivate' to exit this environment and go back to normal terminal.
echo ----- Example usage:
echo python bark_perform.py --history_prompt "bark_infinity\assets\prompts\en_fiery.npz" --text_prompt "I refuse to answer that question on the grounds that I don't know the answer."

echo ----- read prompts from text file
echo `python bark_perform.py --prompt_file myprompts.txt --split_input_into_separate_prompts_by string --split_input_into_separate_prompts_by_value AAAAA --output_dir myprompts_samples`


echo -

@rem activate installer env
call "%CONDA_ROOT_PREFIX%\condabin\conda.bat" activate "%INSTALL_ENV_DIR%" && cd bark || ( echo. && echo Miniconda hook not found. && goto end )

echo Executing --- "%CALL_COMMAND% --prompt_file %PROMPT_FILE% --output_dir %OUTPUT_DIR% --output_filename %OUTPUT_FILENAME% --history_prompt %HISTORY_PROMPT% --hoarder_mode true --hoarder_mode true --text_temp 0.15 --waveform_temp 0.35 --stable_mode_interval 0" ---

@rem enter commands
cmd /c "%CALL_COMMAND% --prompt_file %PROMPT_FILE% --output_dir %OUTPUT_DIR% --output_filename %OUTPUT_FILENAME% --history_prompt %HISTORY_PROMPT% --hoarder_mode true --text_temp 0.15 --waveform_temp 0.35 --stable_mode_interval 0"

@rem echo   --output_dir OUTPUT_DIR
@rem echo   --output_filename OUTPUT_FILENAME
@rem echo   --prompt_file PROMPT_FILE
@rem echo   --history_prompt (choose speaker)
@rem --hoarder_mode HOARDER_MODE


Echo If there is an error, take note of it here.
exit 
@rem PAUSE >nul
:end
pause
