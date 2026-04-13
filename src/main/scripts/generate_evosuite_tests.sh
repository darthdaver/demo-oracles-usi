# Get current directory
current_dir=$(realpath "$(dirname "${BASH_SOURCE[0]}")")
# Setup global variables
source "${current_dir}/global_variables.sh"
# Setup sdkman
source "${current_dir}/init_sdkman.sh" "${SDKMAN_DIR}"

sdk use java "$JAVA8"

if [ ! -d "${OUTPUT_DIR}" ]; then
    mkdir -p "${OUTPUT_DIR}"
fi

java -jar "$EVOSUITE_JAR" \
-base_dir "${OUTPUT_DIR}" \
-Dsearch_budget=10 \
-Dcheck_contracts=true \
-Dstopping_condition=MaxTime \
-target "${PROJECT_JAR}" \
-prefix org.demo.test.oracles \
-seed 42

sdk use java "$JAVA17"

java -jar "$ORACLES_TO_TESTS_JAR" "remove_oracles" "${OUTPUT_DIR}/evosuite-tests"

cd "${OUTPUT_DIR}/evosuite-prefixes"

find . -type f -name '*Failed_ESTest.java' -delete
find . -type f -name '*Failed_ESTest_scaffolding.java' -delete