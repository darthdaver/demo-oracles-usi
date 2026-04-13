# Get current directory
current_dir=$(realpath "$(dirname "${BASH_SOURCE[0]}")")
# Setup global variables
source "${current_dir}/global_variables.sh"
# Setup sdkman
source "${current_dir}/init_sdkman.sh" "${SDKMAN_DIR}"

sdk use java "$JAVA8"

java -jar "$EVOSUITE_JAR" -listParameters