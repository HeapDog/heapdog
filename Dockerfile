FROM ubuntu:24.04

ARG USER=dev
ARG UID=1000

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    sudo \
    curl \
    git \
    wget \
    unzip \
    zip \
    libssl-dev \
    libffi-dev \
    python3 \
    python3-pip \
    python3-venv \
    && rm -rf /var/lib/apt/lists/*

# Delete the default user if it exists
RUN deluser --remove-home ubuntu || true

# Create a non-root user
RUN useradd -m -s /bin/bash -u $UID $USER && \
    echo "$USER ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

USER $USER
WORKDIR /home/$USER

# Install SDKMAN
RUN curl -s "https://get.sdkman.io" | bash && \
    bash -c "source /home/$USER/.sdkman/bin/sdkman-init.sh && sdk install java 25-amzn && sdk install gradle"

RUN bash -c "source /home/$USER/.sdkman/bin/sdkman-init.sh && sdk install java 25-amzn && sdk install gradle"

EXPOSE 8080

CMD [ "sleep", "infinity" ]