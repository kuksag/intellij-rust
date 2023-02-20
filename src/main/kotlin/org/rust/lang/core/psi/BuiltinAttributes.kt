/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.lang.core.psi

import org.rust.lang.core.psi.AttributeDuplicates.*
import org.rust.lang.core.psi.AttributeType.CrateLevel
import org.rust.lang.core.psi.AttributeType.Normal


// Bump `RsFileStub.Type.STUB_VERSION` if modified
// https://github.com/rust-lang/rust/blob/7a8636c843bd24038fe1d1f69b4a8e4b0ea55d4e/compiler/rustc_feature/src/builtin_attrs.rs#L266
val RS_BUILTIN_ATTRIBUTES: Map<String, AttributeInfo> = mapOf(
    "cfg" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "predicate", null), DuplicatesOk, false),
    "cfg_attr" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "predicate, attr1, attr2, ...", null), DuplicatesOk, false),
    "ignore" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, "reason"), WarnFollowing, false),
    "should_panic" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "expected = \"reason\"", "reason"), FutureWarnFollowing, false),
    "reexport_test_harness_main" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "name"), ErrorFollowing, false),
    "automatically_derived" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "macro_use" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "name1, name2, ...", null), WarnFollowingWordOnly, false),
    "macro_escape" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "macro_export" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "local_inner_macros", null), WarnFollowing, false),
    "proc_macro" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), ErrorFollowing, false),
    "proc_macro_derive" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "TraitName, /*opt*/ attributes(name1, name2, ...)", null), ErrorFollowing, false),
    "proc_macro_attribute" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), ErrorFollowing, false),
    "warn" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "lint1, lint2, ..., /*opt*/ reason = \"...\"", null), DuplicatesOk, false),
    "allow" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "lint1, lint2, ..., /*opt*/ reason = \"...\"", null), DuplicatesOk, false),
    "expect" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "lint1, lint2, ..., /*opt*/ reason = \"...\"", null), DuplicatesOk, true),
    "forbid" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "lint1, lint2, ..., /*opt*/ reason = \"...\"", null), DuplicatesOk, false),
    "deny" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "lint1, lint2, ..., /*opt*/ reason = \"...\"", null), DuplicatesOk, false),
    "must_use" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, "reason"), FutureWarnFollowing, false),
    "must_not_suspend" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, "reason"), WarnFollowing, true),
    "deprecated" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "/*opt*/ since = \"version\", /*opt*/ note = \"reason\"", "reason"), ErrorFollowing, false),
    "crate_name" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "name"), FutureWarnFollowing, false),
    "crate_type" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "bin|lib|..."), DuplicatesOk, false),
    "crate_id" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "ignored"), FutureWarnFollowing, false),
    "link" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "name = \"...\", /*opt*/ kind = \"dylib|static|...\", /*opt*/ wasm_import_module = \"...\", /*opt*/ import_name_type = \"decorated|noprefix|undecorated\"", null), DuplicatesOk, false),
    "link_name" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "name"), FutureWarnPreceding, false),
    "no_link" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "repr" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "C", null), DuplicatesOk, false),
    "export_name" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "name"), FutureWarnPreceding, false),
    "link_section" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "name"), FutureWarnPreceding, false),
    "no_mangle" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "used" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "compiler|linker", null), WarnFollowing, false),
    "link_ordinal" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "ordinal", null), ErrorPreceding, false),
    "recursion_limit" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "N"), FutureWarnFollowing, false),
    "type_length_limit" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "N"), FutureWarnFollowing, false),
    "const_eval_limit" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "N"), ErrorFollowing, true),
    "move_size_limit" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "N"), ErrorFollowing, true),
    "unix_sigpipe" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, "inherit|sig_ign|sig_dfl"), ErrorFollowing, true),
    "start" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "no_start" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(true, null, null), WarnFollowing, false),
    "no_main" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(true, null, null), WarnFollowing, false),
    "path" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "file"), FutureWarnFollowing, false),
    "no_std" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(true, null, null), WarnFollowing, false),
    "no_implicit_prelude" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "non_exhaustive" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "windows_subsystem" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, null, "windows|console"), FutureWarnFollowing, false),
    "panic_handler" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "inline" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "always|never", null), FutureWarnFollowing, false),
    "cold" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "no_builtins" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(true, null, null), WarnFollowing, false),
    "target_feature" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "enable = \"name\"", null), DuplicatesOk, false),
    "track_caller" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, false),
    "instruction_set" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "set", null), ErrorPreceding, false),
    "no_sanitize" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "address, kcfi, memory, thread", null), DuplicatesOk, true),
    "no_coverage" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "doc" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "hidden|inline|...", "string"), DuplicatesOk, false),

    // Unstable attributes:

    "debugger_visualizer" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "natvis_file = \"...\", gdb_script_file = \"...\"", null), DuplicatesOk, true),
    "naked" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "plugin" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, "name", null), DuplicatesOk, true),
    "test_runner" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, "path", null), ErrorFollowing, true),
    "marker" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "thread_local" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "no_core" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(true, null, null), WarnFollowing, true),
    "optimize" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "size|speed", null), ErrorPreceding, true),
    "ffi_returns_twice" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "ffi_pure" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "ffi_const" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "register_tool" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, "tool1, tool2, ...", null), DuplicatesOk, true),
    "cmse_nonsecure_entry" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "const_trait" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "deprecated_safe" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "since = \"version\", note = \"...\"", null), ErrorFollowing, true),
    "collapse_debuginfo" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "do_not_recommend" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "feature" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(false, "name1, name2, ...", null), DuplicatesOk, false),
    "stable" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "feature = \"name\", since = \"version\"", null), DuplicatesOk, false),
    "unstable" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "feature = \"name\", reason = \"...\", issue = \"N\"", null), DuplicatesOk, false),
    "rustc_const_unstable" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "feature = \"name\"", null), DuplicatesOk, false),
    "rustc_const_stable" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "feature = \"name\"", null), DuplicatesOk, false),
    "rustc_default_body_unstable" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "feature = \"name\", reason = \"...\", issue = \"N\"", null), DuplicatesOk, false),
    "allow_internal_unstable" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "feat1, feat2, ...", null), DuplicatesOk, true),
    "rustc_allow_const_fn_unstable" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "feat1, feat2, ...", null), DuplicatesOk, true),
    "allow_internal_unsafe" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_safe_intrinsic" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), DuplicatesOk, false),
    "rustc_allowed_through_unstable_modules" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "fundamental" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "may_dangle" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_allocator" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_nounwind" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_reallocator" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_deallocator" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_allocator_zeroed" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "default_lib_allocator" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "needs_allocator" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "panic_runtime" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "needs_panic_runtime" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "compiler_builtins" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "profiler_runtime" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "linkage" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "external|internal|..."), ErrorPreceding, true),
    "rustc_std_internal_symbol" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_builtin_macro" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "name, /*opt*/ attributes(name1, name2, ...)", null), ErrorFollowing, true),
    "rustc_proc_macro_decls" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_macro_transparency" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "transparent|semitransparent|opaque"), ErrorFollowing, true),
    "rustc_on_unimplemented" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "/*opt*/ message = \"...\", /*opt*/ label = \"...\", /*opt*/ note = \"...\"", "message"), ErrorFollowing, true),
    "rustc_conversion_suggestion" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_trivial_field_reads" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_lint_query_instability" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_lint_diagnostics" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_lint_opt_ty" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_lint_opt_deny_field_access" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "message", null), WarnFollowing, true),
    "rustc_promotable" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_legacy_const_generics" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "N", null), ErrorFollowing, true),
    "rustc_do_not_const_check" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_layout_scalar_valid_range_start" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "value", null), ErrorFollowing, true),
    "rustc_layout_scalar_valid_range_end" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "value", null), ErrorFollowing, true),
    "rustc_nonnull_optimization_guaranteed" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "lang" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "name"), DuplicatesOk, true),
    "rustc_pass_by_value" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), ErrorFollowing, true),
    "rustc_coherence_is_core" to BuiltinAttributeInfo(CrateLevel, AttributeTemplate(true, null, null), ErrorFollowing, true),
    "rustc_coinductive" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_allow_incoherent_impl" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), ErrorFollowing, true),
    "rustc_deny_explicit_impl" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), ErrorFollowing, true),
    "rustc_has_incoherent_inherent_impls" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), ErrorFollowing, true),
    "rustc_box" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), ErrorFollowing, true),
    "rustc_diagnostic_item" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "name"), ErrorFollowing, true),
    "prelude_import" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_paren_sugar" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_inherit_overflow_checks" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_reservation_impl" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "reservation message"), ErrorFollowing, true),
    "rustc_test_marker" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, null, "name"), WarnFollowing, true),
    "rustc_unsafe_specialization_marker" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_specialization_trait" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_main" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_skip_array_during_method_dispatch" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_must_implement_one_of" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "function1, function2, ...", null), ErrorFollowing, true),
    "rustc_effective_visibility" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_outlives" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_capture_analysis" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_insignificant_dtor" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_strict_coherence" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_variance" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_layout" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "field1, field2, ...", null), WarnFollowing, true),
    "rustc_regions" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_error" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "delay_span_bug_from_inside_query", null), WarnFollowingWordOnly, true),
    "rustc_dump_user_substs" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_evaluate_where_clauses" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_if_this_changed" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, "DepNode", null), DuplicatesOk, true),
    "rustc_then_this_would_need" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "DepNode", null), DuplicatesOk, true),
    "rustc_clean" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "cfg = \"...\", /*opt*/ label = \"...\", /*opt*/ except = \"...\"", null), DuplicatesOk, true),
    "rustc_partition_reused" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "cfg = \"...\", module = \"...\"", null), DuplicatesOk, true),
    "rustc_partition_codegened" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "cfg = \"...\", module = \"...\"", null), DuplicatesOk, true),
    "rustc_expected_cgu_reuse" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "cfg = \"...\", module = \"...\", kind = \"...\"", null), DuplicatesOk, true),
    "rustc_symbol_name" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_polymorphize_error" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_def_path" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_mir" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "arg1, arg2, ...", null), DuplicatesOk, true),
    "custom_mir" to BuiltinAttributeInfo(Normal, AttributeTemplate(false, "dialect = \"...\", phase = \"...\"", null), ErrorFollowing, true),
    "rustc_dump_program_clauses" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_dump_env_program_clauses" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_object_lifetime_default" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_dump_vtable" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),
    "rustc_dummy" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), DuplicatesOk, true),
    "omit_gdb_pretty_printer_section" to BuiltinAttributeInfo(Normal, AttributeTemplate(true, null, null), WarnFollowing, true),

    // Internal stdlib proc macros

    "simd_test" to BuiltinProcMacroInfo,
    "assert_instr" to BuiltinProcMacroInfo,

    // Proc macros from stdlib. Defined like
    // `pub macro test($item:item) {}`
    // TODO resolve them correctly and remove from this list?

    "derive" to BuiltinProcMacroInfo,
    "test" to BuiltinProcMacroInfo,
    "bench" to BuiltinProcMacroInfo,
    "test_case" to BuiltinProcMacroInfo,
    "global_allocator" to BuiltinProcMacroInfo,
    "cfg_accessible" to BuiltinProcMacroInfo,
)

// https://github.com/rust-lang/rust/blob/76d18cfb8945f824c8777e04981e930d2037954e/compiler/rustc_resolve/src/macros.rs#L153
val RS_BUILTIN_TOOL_ATTRIBUTES = setOf("rustfmt", "clippy")

sealed interface AttributeInfo

data class BuiltinAttributeInfo(
    val type: AttributeType,
    val template: AttributeTemplate,
    val duplicates: AttributeDuplicates,
    val gated: Boolean,
): AttributeInfo

object BuiltinProcMacroInfo: AttributeInfo

enum class AttributeType {
    Normal, CrateLevel
}

data class AttributeTemplate(
    val word: Boolean,
    val list: String?,
    val nameValueStr: String?,
)

enum class AttributeDuplicates {
    DuplicatesOk,
    WarnFollowing,
    WarnFollowingWordOnly,
    ErrorFollowing,
    ErrorPreceding,
    FutureWarnFollowing,
    FutureWarnPreceding
}
