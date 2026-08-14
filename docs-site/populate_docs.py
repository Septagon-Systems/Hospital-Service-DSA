import os
import shutil

base_docs = '/home/ahorts/Documents/Projects/308/Hospital-Service-DSA/docs'
site_docs = '/home/ahorts/Documents/Projects/308/Hospital-Service-DSA/docs-site/src/content/docs'
public_dir = '/home/ahorts/Documents/Projects/308/Hospital-Service-DSA/docs-site/public'

categories = [
    f'{site_docs}/overview',
    f'{site_docs}/data-structures/linear',
    f'{site_docs}/data-structures/trees',
    f'{site_docs}/data-structures/hashing',
    f'{site_docs}/algorithms/searching',
    f'{site_docs}/algorithms/sorting',
    f'{site_docs}/algorithms/graph',
    f'{site_docs}/algorithms/optimization',
]

for cat in categories:
    os.makedirs(cat, exist_ok=True)

# Copy SVG diagram assets for Red-Black Tree and others
src_images = f'{base_docs}/trace-tables/images'
if os.path.exists(src_images):
    shutil.copytree(src_images, f'{site_docs}/data-structures/trees/images', dirs_exist_ok=True)
    shutil.copytree(src_images, f'{public_dir}/images', dirs_exist_ok=True)
    print('Copied diagram SVG assets!')

mappings = {
    # Overview
    f'{base_docs}/dev-log.md': f'{site_docs}/overview/dev-log.md',
    
    # Linear Data Structures
    f'{base_docs}/trace-tables/07_dynamic_array_list.md': f'{site_docs}/data-structures/linear/dynamic-array.md',
    f'{base_docs}/trace-tables/array-stack-trace.md': f'{site_docs}/data-structures/linear/array-stack.md',
    f'{base_docs}/trace-tables/array-deque-trace.md': f'{site_docs}/data-structures/linear/array-deque.md',
    f'{base_docs}/trace-tables/07_circular_queue_trace.md': f'{site_docs}/data-structures/linear/circular-queue.md',
    f'{base_docs}/proof-sketches/04_linked_list_loop_invariant.md': f'{site_docs}/data-structures/linear/linked-list-proof.md',

    # Trees & Priority Structures
    f'{base_docs}/trace-tables/10_binary_search_tree.md': f'{site_docs}/data-structures/trees/binary-search-tree.md',
    f'{base_docs}/trace-tables/08_red_black_tree.md': f'{site_docs}/data-structures/trees/red-black-tree.md',
    f'{base_docs}/trace-tables/min-heap-dispatch-trace.md': f'{site_docs}/data-structures/trees/min-heap-dispatch.md',

    # Hashing & Maps
    f'{base_docs}/trace-tables/11_custom_hash_table.md': f'{site_docs}/data-structures/hashing/custom-hash-table.md',
    f'{base_docs}/trace-tables/12_custom_map_set_trace.md': f'{site_docs}/data-structures/hashing/custom-map-set.md',

    # Searching Algorithms
    f'{base_docs}/trace-tables/01_binary_search.md': f'{site_docs}/algorithms/searching/binary-search.md',
    f'{base_docs}/counterexamples/02_invalid_precondition_case.md': f'{site_docs}/algorithms/searching/unsorted-precondition.md',

    # Sorting Algorithms
    f'{base_docs}/trace-tables/02_insertion_sort.md': f'{site_docs}/algorithms/sorting/insertion-sort.md',
    f'{base_docs}/trace-tables/09_selection_sort.md': f'{site_docs}/algorithms/sorting/selection-sort.md',
    f'{base_docs}/trace-tables/03_merge_quick_sort.md': f'{site_docs}/algorithms/sorting/merge-quick-sort.md',
    f'{base_docs}/proof-sketches/01_loop_invariant_search_sort.md': f'{site_docs}/algorithms/sorting/search-sort-loop-invariants.md',

    # Graph Algorithms
    f'{base_docs}/trace-tables/04_dijkstra.md': f'{site_docs}/algorithms/graph/dijkstra.md',
    f'{base_docs}/trace-tables/05_kruskal_prim.md': f'{site_docs}/algorithms/graph/kruskal-prim.md',

    # Optimization Algorithms
    f'{base_docs}/trace-tables/06_dynamic_programming.md': f'{site_docs}/algorithms/optimization/dynamic-programming.md',
    f'{base_docs}/counterexamples/01_greedy_failure_case.md': f'{site_docs}/algorithms/optimization/greedy-failure.md',
    f'{base_docs}/proof-sketches/03_greedy_dp_correctness.md': f'{site_docs}/algorithms/optimization/greedy-dp-correctness.md',
    f'{base_docs}/proof-sketches/02_recursion_induction_proof.md': f'{site_docs}/algorithms/optimization/recursion-induction.md',
}

for src, dst in mappings.items():
    if os.path.exists(src):
        with open(src, 'r') as f:
            content = f.read()
        
        # Ensure valid frontmatter title for Starlight
        if not content.startswith('---'):
            lines = [l.strip() for l in content.split('\n') if l.strip()]
            title = lines[0].replace('#', '').strip() if lines else 'Documentation'
            content = f'---\ntitle: "{title}"\n---\n\n' + content

        with open(dst, 'w') as f:
            f.write(content)

print('All markdown files and diagram assets populated successfully!')
