ROOT_DIR=$(dirname "$(dirname "$(dirname "$(realpath "$(dirname "${BASH_SOURCE[0]}")")")")")

SCRIPTS_DIR=$(realpath "$(dirname "${BASH_SOURCE[0]}")")
RESOURCES_DIR="${SCRIPTS_DIR}/resources"
SDKMAN_DIR="${SCRIPTS_DIR}/resources/sdkman"
PROJECT_BIN_DIR="${ROOT_DIR}/target/classes"
OUTPUT_DIR="${ROOT_DIR}/output"

# Package
PROJECT_PACKAGE="org.demo.test.oracles"

# Jars
EVOSUITE_JAR="${RESOURCES_DIR}/evosuite-1.0.6.jar"
JDOCTOR_JAR="${RESOURCES_DIR}/jdoctor.jar"
PROJECT_JAR="${RESOURCES_DIR}/demo-test-oracles.jar"
RANDOOP_JAR="${RESOURCES_DIR}/randoop-4.3.2/randoop-all-4.3.2.jar"
ORACLES_TO_TESTS_JAR="${RESOURCES_DIR}/oracles-to-test.jar"

# Sdkman Java versions
JAVA17="17.0.8-oracle"
JAVA8="8.0.392-amzn"
# Sdkman Maven version
MAVEN_VERSION="3.9.4"

# Links
JDOCTOR_LINK="https://drive.switch.ch/index.php/s/dAkxslN83PvXhLo/download"
EVOSUITE_JAR_LINK="https://drive.switch.ch/index.php/s/oa098Miqj8IQcOO/download"
PROJECT_LINK="https://drive.switch.ch/index.php/s/GwJcZuSFiqJMfHl/download"
ORACLES_TO_TESTS_JAR_LINK="https://drive.switch.ch/index.php/s/c11jDorB9CeMUw9/download"
