"""
This module is the utility functions used in e2e pipeline creation
"""

from csv import reader
from spinnaker_pipeline_creation import configuration
from spinnaker_pipeline_creation.etc import request_retry

CONSTANTS = configuration.ApplicationConfig()
GERRIT_OSS_CCPR = CONSTANTS.get('GERRIT_URLS', 'oss_ccpr')
GERRIT_PARAMETERS = ';a=blob_plain;hb=refs/heads/master;f='
GERRIT_FILE_URL = GERRIT_OSS_CCPR + GERRIT_PARAMETERS


def update_file_with_remote_data(remote_filepath, local_filepath, gerrit_creds):
    """
    Function to update the local file with the data from the remote file
    :param remote_filepath: URL of remote file path
    :param local_filepath: the local file path where data need to be updated to
    :param gerrit_creds: the gerrit user needed to access gerrit
    :return: None
    """
    url = f'https://{gerrit_creds}@{GERRIT_FILE_URL}{remote_filepath}'
    remote_file = (request_retry.request_retry("GET", url, 5)).text
    with open(local_filepath, 'w') as file_buffer:
        file_buffer.write(remote_file)


def get_headers(parameter_filepath):
    """
    Function reads the parameter CSV file and returns the headers
    :param parameter_filepath: the local file path of the parameter file
    :return: headers
    """
    with open(parameter_filepath, 'r') as read_obj:
        csv_reader = reader(read_obj)
        headers = []
        for row in csv_reader:
            headers = row
            break
    return headers
