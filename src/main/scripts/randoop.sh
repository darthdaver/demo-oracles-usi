# Get current directory
current_dir=$(realpath "$(dirname "${BASH_SOURCE[0]}")")
# Setup global variables
source "${current_dir}/global_variables.sh"
# Setup sdkman
source "${current_dir}/init_sdkman.sh" "${SDKMAN_DIR}"

sdk use java "$JAVA8"

randoop_output_dir="${OUTPUT_DIR}/randoop-tests"

if [ ! -d "${randoop_output_dir}" ]; then
    mkdir -p "${randoop_output_dir}"
fi

java -classpath ${PROJECT_JAR}:${RANDOOP_JAR} randoop.main.Main gentests \
--test-package="$PROJECT_PACKAGE" \
--time-limit=60 \
--stop-on-error-test=true \
--junit-output-dir="$randoop_output_dir"