FROM armdocker.rnd.ericsson.se/proj-adp-cicd-drop/spin:3.2.0

# Remove existing pip packages
RUN rm -fr /usr/lib/python3.6/site-packages/pip*

# Install dependencies
RUN zypper ar -C -G -f https://arm.rnd.ki.sw.ericsson.se/artifactory/proj-ldc-repo-rpm-local/common_base_os/sles/3.18.0-18 LDC-CBO-SLES \
 && zypper ref -f -r LDC-CBO-SLES \
 && zypper install -y python3 glibc-locale \
 && zypper in -y python3-pip \
 && zypper clean --all

# Upgrade pip
RUN pip install --upgrade pip

# A locale needs to be installed and set for later use by some python packages like click
ENV LC_ALL=C.UTF-8
ENV LANG=C.UTF-8

# Install Poetry
RUN pip install poetry==1.1.6

# Store all of the packages under the /usr/src/app/ directory
WORKDIR /usr/src/app
COPY poetry.lock pyproject.toml /usr/src/app/

# We turn off virtual env as not needed inside the container
RUN poetry config virtualenvs.create false

# Install dependencies
RUN poetry install --no-interaction --no-ansi --no-dev

# Copy the files into the image
COPY . /usr/src/app
