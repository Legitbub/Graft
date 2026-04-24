import re
import sys

def remove_comments(file_path):
    with open(file_path, 'r') as f:
        content = f.read()
    
    # Remove single line comments that are on their own line (with optional leading whitespace)
    # Also remove single line comments at the end of the line
    # We want to be careful not to remove comments inside strings, but for this file it's fine.
    # The file doesn't have URLs in strings like http://
    
    # Replace // ... on a line by itself
    content = re.sub(r'^\s*//.*$\n', '', content, flags=re.MULTILINE)
    
    # Replace // ... at the end of a line
    content = re.sub(r'\s*//.*$', '', content, flags=re.MULTILINE)

    with open(file_path, 'w') as f:
        f.write(content)

if __name__ == "__main__":
    remove_comments(sys.argv[1])
