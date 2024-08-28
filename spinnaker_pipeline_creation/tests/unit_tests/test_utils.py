"""
Unit tests for utils.py
"""

from spinnaker_pipeline_creation.etc import utils
from spinnaker_pipeline_creation.etc import request_retry
from spinnaker_pipeline_creation.tests.unit_tests.mock_response import MockResponse


# pylint: disable=no-self-use
class TestUtils:
    """
    Class to run unit tests for utils.py
    """

    def test_update_file_write_data_successfully(self, monkeypatch, tmp_path):
        """
        Test that the function to updates the local file with the data from the remote file
        :param monkeypatch:
        :param tmp_path:
        """
        valid_content = "some text"

        # pylint: disable=unused-argument
        def request_mock(*args):
            """
            A mock for a successful request
            :param args:
            :return: Mock Response
            :rtype: MockResponse
            """
            nonlocal valid_content
            return MockResponse(valid_content, 200)

        monkeypatch.setattr(request_retry, 'request_retry', request_mock)
        local_path = tmp_path / 'test.txt'
        utils.update_file_with_remote_data('some_remote_url', local_path, 'gerrit_test_user')
        with open(local_path) as file_buf:
            assert file_buf.read() == valid_content

    def test_get_headers(self, tmp_path):
        """
        Test that the function reads the parameter CSV file and returns the headers
        :param tmp_path:
        """
        test_headers = 'STR1,STR2,STR3'
        actual_headers = ['STR1', 'STR2', 'STR3']
        dir_path = tmp_path / 'test_dir'
        dir_path.mkdir()
        file_path = dir_path / "test.csv"
        with open(file_path, 'w') as file_buf:
            file_buf.write(test_headers)
        expected_headers = utils.get_headers(file_path)
        assert actual_headers == expected_headers
