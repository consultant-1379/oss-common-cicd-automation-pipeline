class PipelineData:
    """Class for passing pipeline data to functions"""
    area: str
    flows: str
    parameter_filename: str
    template_filename: str

    def __init__(self, area: str, flows: str, parameter_filename: str, template_filename: str):
        self.area = area
        self.flows = flows
        self.parameter_filename = parameter_filename
        self.template_filename = template_filename
