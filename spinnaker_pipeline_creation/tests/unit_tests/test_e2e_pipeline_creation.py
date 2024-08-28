"""
Unit tests for e2e_pipeline_creation.py
"""

import os
import pytest
from spinnaker_pipeline_creation import e2e_pipeline_creation
from spinnaker_pipeline_creation.etc import utils
from spinnaker_pipeline_creation import configuration
from spinnaker_pipeline_creation.pipeline_data import PipelineData

CONSTANTS = configuration.ApplicationConfig()
RUNTIME_TEMP_FILES_DIR_PATH = CONSTANTS.get('DIR_PATHS', 'runtime_temp_files_dev')
PARAMETER = CONSTANTS.get('FILE_NAMES', 'parameter')
PIPELINE_TEMPLATE = CONSTANTS.get('FILE_NAMES', 'pipeline_template')
PIPELINE = CONSTANTS.get('FILE_NAMES', 'pipeline')
PARAMETER_FILEPATH = RUNTIME_TEMP_FILES_DIR_PATH + PARAMETER
PIPELINE_TEMPLATE_PATH = RUNTIME_TEMP_FILES_DIR_PATH + PIPELINE_TEMPLATE
PIPELINE_PATH = RUNTIME_TEMP_FILES_DIR_PATH + PIPELINE
SPIN_COMMAND = "spin pipeline save --config /mnt/.spin/config --file " + PIPELINE_PATH


def teardown_module():
    """
    This is the teardown fuction for this module.
    """
    files_to_clean = [PARAMETER_FILEPATH, PIPELINE_TEMPLATE_PATH, PIPELINE_PATH]
    for file_path in files_to_clean:
        with open(file_path, "w"):
            pass


# pylint: disable=no-self-use,unused-argument
class TestE2EPipelineCreation:
    """
    Class to run unit tests for e2e_pipeline_creation.py
    """

    @pytest.fixture
    def mock_update_file(self, monkeypatch):
        """
        This fixture is used to mock update_file function
        :param monkeypatch:
        """
        def update_file_mock(remote_filepath, local_filepath, gerrit_creds):
            """
            This function is called in place of update_file function and performs specific function
            based on the  parameters of the original function
            :param remote_filepath:
            :param local_filepath:
            :param gerrit_creds:
            """
            def update_parameter_file_mock(*args):
                """
                A mock for a update parameter file
                :param args:
                """
                test_parameter = ['HEADER1,HEADER2,HEADER3\n', 'VALUE1,VALUE2,VALUE3']
                with open(args[0], 'w') as file_buf:
                    file_buf.writelines(test_parameter)

            def update_pipeline_template_file_mock(*args):
                """
                A mock for a update pipeline template file
                :param args:
                """
                test_template = '{\n\t"KEY1": "HEADER1",\n\t"KEY2": "HEADER2",\n\t"KEY3": "HEADER3"\n}'
                with open(args[0], 'w') as file_buf:
                    file_buf.writelines(test_template)

            if remote_filepath == 'cicd_pipelines_parameters_and_templates/testing' + \
                                  '/testing_template/parameter_files/test_pipeline.csv':
                update_parameter_file_mock(local_filepath)
            elif remote_filepath == 'cicd_pipelines_parameters_and_templates/testing' + \
                                    '/testing_template/pipeline_template/test_pipeline.json':
                update_pipeline_template_file_mock(local_filepath)

        monkeypatch.setattr(utils, 'update_file_with_remote_data', update_file_mock)

    def test_pipeline_creation_or_updation(self, mock_update_file, mocker):
        """
        Test that the function reads the paramter csv and update the pipeline json correctly and
        also that the correct spin command is called.
        :param mock_update_file:
        :param mocker:
        """
        valid_template = '{\n\t"KEY1": "VALUE1",\n\t"KEY2": "VALUE2",\n\t"KEY3": "VALUE3"\n}'
        mocker.patch('os.system')
        e2e_pipeline_creation.create_update_pipeline(PipelineData(
            'testing',
            'testing_template',
            'test_pipeline.csv',
            'test_pipeline.json'),
            'gerrit_test_user',
            dev_mode=True)
        with open(PIPELINE_PATH) as file_buf:
            assert file_buf.read() == valid_template
        os.system.assert_called_once_with(SPIN_COMMAND)
