"""
This is the CLI for the Automation pipeline management tool.
"""
import click

from spinnaker_pipeline_creation.e2e_pipeline_creation import create_update_pipeline
from spinnaker_pipeline_creation.pipeline_data import PipelineData


def area_option(func):
    """A decorator for the parameter_filename command line argument."""
    return click.option('-ar', '--area', type=click.STRING, required=True,
                        help='The area associated with the pipeline you want to generate or update. '
                             'It used for pipeline creation or updation.')(func)


def flows_option(func):
    """A decorator for the flows command line argument."""
    return click.option('-fl', '--flows', type=click.STRING, required=True,
                        help='The flows associated with the pipeline you want to generate or update. '
                             'It used for pipeline creation or updation.')(func)


def parameter_filename_option(func):
    """A decorator for the parameter_filename command line argument."""
    return click.option('-pf', '--parameter_filename', type=click.STRING, required=True,
                        help='The filename of the parameter file. '
                             'It used for pipeline creation or updation.')(func)


def pipeline_template_filename_option(func):
    """A decorator for the pipeline_template_filename command line argument."""
    return click.option('-ptf', '--pipeline_template_filename', type=click.STRING, required=True,
                        help='The filename of the pipeline template file. '
                             'It used for pipeline creation or updation.')(func)


def parameter_file_path_option(func):
    """A decorator: for the full_parameter_filepath command line argument."""
    return click.option('-fpf', '--full_parameter_filepath', type=click.STRING, required=True,
                        help='The full file path of the parameter file. '
                             'It used for pipeline creation or updation for test pipelines.')(func)


def template_file_path_option(func):
    """A decorator for the full_pipeline_template_filepath command line argument."""
    return click.option('-fptf', '--full_pipeline_template_filepath', type=click.STRING, required=True,
                        help='The full file path of the pipeline template file. '
                             'It used for pipeline creation or updation for test pipelines.')(func)


def gerrit_creds_option(func):
    """A decorator for the full_pipeline_template_filepath command line argument."""
    return click.option('-gc', '--gerrit_creds', type=click.STRING, required=True,
                        help='Gerrit user credentials that are needed to read files from Gerrit. '
                             'It used for pipeline creation or updation for test pipelines.')(func)


@click.group()
def cli_main():
    """
    The entry-point to the Automation pipeline management tool.
    Please see available options below.
    """


@cli_main.command()
@area_option
@flows_option
@parameter_filename_option
@pipeline_template_filename_option
@gerrit_creds_option
def create_or_update_pipeline(area, flows, parameter_filename, pipeline_template_filename, gerrit_creds):
    """
    Creates or updates pipeline using parameter and pipeline template files.
    Then it waits for the request to be resolved or to timeout.
    :param area:
    :param flows:
    :param parameter_filename:
    :param pipeline_template_filename:
    :param gerrit_creds:
    """
    create_update_pipeline(PipelineData(area, flows, parameter_filename, pipeline_template_filename),
                           gerrit_creds)


@cli_main.command()
@parameter_file_path_option
@template_file_path_option
@gerrit_creds_option
def create_or_update_test_pipeline(full_parameter_filepath, full_pipeline_template_filepath, gerrit_creds):
    """
    Creates or updates pipeline using parameter and pipeline template files.
    Then it waits for the request to be resolved or to timeout.
    :param full_parameter_filepath:
    :param full_pipeline_template_filepath:
    :param gerrit_creds:
    :return: None
    """
    area_file_path_segment = 1
    flows_file_path_segment = 2
    payload_file_path_segment = 4

    parameter_filepath_segments = full_parameter_filepath.split('/')
    area = parameter_filepath_segments[area_file_path_segment]
    flows = parameter_filepath_segments[flows_file_path_segment]
    parameter_filename = parameter_filepath_segments[payload_file_path_segment]

    pipeline_template_filepath_segments = full_pipeline_template_filepath.split('/')
    template_filename = pipeline_template_filepath_segments[payload_file_path_segment]
    create_update_pipeline(PipelineData(area, flows, parameter_filename, template_filename),
                           gerrit_creds, local_mode=True)
