# get current directory
current_dir=$(realpath "$(dirname "${BASH_SOURCE[0]}")")
# Setup global variables
source "${current_dir}/global_variables.sh"
cd "${ROOT_DIR}" || exit 1

if [ ! -d "${RESOURCES_DIR}" ]; then
    mkdir -p "${RESOURCES_DIR}"
fi

# download sdkman
bash "${SCRIPTS_DIR}/install_sdkman.sh"
source "${SCRIPTS_DIR}/init_sdkman.sh"

# Install Java 17
sdk install java "$JAVA17"
sdk install java "$JAVA8"
# Install maven
sdk install maven "$MAVEN_VERSION"

# Download resources
wget -O "${PROJECT_JAR}" "${PROJECT_LINK}"
wget -O "${JDOCTOR_JAR}" "${JDOCTOR_LINK}"
wget -O "${EVOSUITE_JAR}" "${EVOSUITE_JAR_LINK}"
wget -O "${ORACLES_TO_TESTS_JAR}" "${ORACLES_TO_TESTS_JAR_LINK}"