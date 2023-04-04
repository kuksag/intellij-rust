mod formatter;
mod compiler_features;

const COMPILER_FEATURES_PATH: &str = "src/main/resources/compiler-info/compiler-features.json";

fn main() {
    compiler_features::update_compiler_features(COMPILER_FEATURES_PATH);
}
