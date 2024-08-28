"""
This module allows a user to roll out or update spinnaker pipelines using
the pipeline JSON templates and parameter CSV files
"""

import os
from csv import DictReader
from spinnaker_pipeline_creation import configuration
from spinnaker_pipeline_creation.etc import utils
from spinnaker_pipeline_creation.pipeline_data import PipelineData


def create_update_pipeline(pipeline_data: PipelineData, gerrit_creds, local_mode=False, dev_mode=False):
    """
    Function runs a loop replacing the placeholder values in the pipeline JSON template file
    with the values from the parameter CSV file and then update spinnaker pipeline
    using that updated JSON file.
    :param pipeline_data:
    :param gerrit_creds:
    :param local_mode:
    :param dev_mode:
    :return: None
    """
    constants = configuration.ApplicationConfig()
    runtime_temp_files_dir_path = constants.get(
        'DIR_PATHS',
        'runtime_temp_files_dev' if dev_mode else 'runtime_temp_files'
    )
    parameter_filepath = (f'{runtime_temp_files_dir_path}'
                          f'{constants.get("FILE_NAMES", "parameter")}')
    pipeline_template_path = (f'{runtime_temp_files_dir_path}'
                              f'{constants.get("FILE_NAMES", "pipeline_template")}')
    pipeline_path = (f'{runtime_temp_files_dir_path}'
                     f'{constants.get("FILE_NAMES", "pipeline")}')
    remote_parameter_filepath = (f'cicd_pipelines_parameters_and_templates/{pipeline_data.area}'
                                 f'/{pipeline_data.flows}/parameter_files/{pipeline_data.parameter_filename}')
    remote_pipeline_template_filepath = (f'cicd_pipelines_parameters_and_templates/{pipeline_data.area}'
                                         f'/{pipeline_data.flows}/pipeline_template/{pipeline_data.template_filename}')
    if not local_mode:
        utils.update_file_with_remote_data(remote_parameter_filepath, parameter_filepath, gerrit_creds)
        utils.update_file_with_remote_data(remote_pipeline_template_filepath, pipeline_template_path, gerrit_creds)
        replace_values_based_on_csv_and_issue_command(
            parameter_filepath,
            pipeline_template_path,
            pipeline_path,
            utils.get_headers(parameter_filepath)
        )
    else:
        replace_values_based_on_csv_and_issue_command(
            f'/mnt/spinnaker-pipelines/{remote_parameter_filepath}',
            f'/mnt/spinnaker-pipelines/{remote_pipeline_template_filepath}',
            '/mnt/spinnaker-pipelines/pipeline.json',
            utils.get_headers(f'/mnt/spinnaker-pipelines/{remote_parameter_filepath}')
        )


def replace_values_based_on_csv_and_issue_command(parameter_filepath, pipeline_template_path, pipeline_path, headers):
    """ Function replaces values based on the passed-in CSV file
        and issues Spinnaker CLI command.
        :param parameter_filepath:
        :param pipeline_template_path:
        :param pipeline_path:
    """
    spin_command = "spin pipeline save --config /mnt/.spin/config --file " + pipeline_path
    with open(parameter_filepath, 'r') as read_obj:
        csv_dict_reader = DictReader(read_obj)
        for row in csv_dict_reader:
            with open(pipeline_template_path, "rt") as file_obj:
                data = file_obj.read()
                for header in headers:
                    data = data.replace(header, row[header])
            with open(pipeline_path, "w") as file_obj:
                file_obj.write(data)
            os.system(spin_command)
