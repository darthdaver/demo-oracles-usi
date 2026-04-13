#!/bin/bash

# Get current directory
current_dir=$(realpath "$(dirname "${BASH_SOURCE[0]}")")
# Setup global variables
source "${current_dir}/global_variables.sh"
# Setup sdkman
source "${current_dir}/init_sdkman.sh" "${SDKMAN_DIR}"

target_classes=( "Item" "Main" "Order" "ShoppingCart" )
evosuite_prefix_path="${OUTPUT_DIR}/evosuite-prefixes"
src_dir="${ROOT_DIR}/src/main/java"
jdoctor_output_path="${OUTPUT_DIR}/jdoctor-oracles"

if [ ! -d "${jdoctor_output_path}" ]; then
    mkdir -p "${jdoctor_output_path}"
fi

for target_class in "${target_classes[@]}"; do
  jdoctor_output_file="${jdoctor_output_path}/${target_class}_output.json"
  echo "${PROJECT_PACKAGE}.${target_class}"

  sdk use java "$JAVA8"

  java -jar "$JDOCTOR_JAR" \
    --target-class "${PROJECT_PACKAGE}.${target_class}" \
    --source-dir "${src_dir}" \
    --class-dir "${PROJECT_JAR}" \
    --condition-translator-output "${jdoctor_output_file}" \
    --disable-semantics true

  sdk use java "$JAVA17"

  java -jar "$ORACLES_TO_TESTS_JAR" "generate_oracles_output" "${jdoctor_output_file}"
  java -jar "$ORACLES_TO_TESTS_JAR" "insert_oracles" "${evosuite_prefix_path}" "${jdoctor_output_path}" "${PROJECT_JAR}"
done
