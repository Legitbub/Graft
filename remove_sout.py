import sys
import os

def remove_sout(content):
    res = []
    i = 0
    n = len(content)
    while i < n:
        # Look for System.out.println
        idx = content.find("System.out.println", i)
        if idx == -1:
            res.append(content[i:])
            break
        
        # append everything up to idx
        res.append(content[i:idx])
        i = idx
        
        # Find the first '(' after System.out.println
        paren_idx = content.find("(", i)
        # Verify it's immediately after (with optional spaces)
        space_between = content[i+18:paren_idx].strip()
        if paren_idx == -1 or len(space_between) > 0:
            res.append(content[i:i+18])
            i += 18
            continue
        
        # Match parentheses
        open_parens = 1
        curr = paren_idx + 1
        in_string = False
        escape = False
        
        while curr < n and open_parens > 0:
            c = content[curr]
            if in_string:
                if escape:
                    escape = False
                elif c == '\\':
                    escape = True
                elif c == '"':
                    in_string = False
            else:
                if c == '"':
                    in_string = True
                elif c == '(':
                    open_parens += 1
                elif c == ')':
                    open_parens -= 1
            curr += 1
            
        # curr is now at the character after the closing ')'
        # now find the semicolon
        while curr < n and content[curr] in ' \t\n\r':
            curr += 1
            
        if curr < n and content[curr] == ';':
            curr += 1
            # remove trailing whitespace up to newline
            while curr < n and content[curr] in ' \t':
                curr += 1
            if curr < n and content[curr] == '\n':
                curr += 1
            elif curr < n and content[curr] == '\r' and curr+1 < n and content[curr+1] == '\n':
                curr += 2
                
            # also remove leading whitespaces of the System.out.println line if we are removing the whole line
            # actually we appended spaces to `res` before `idx`. We can backtrack and remove spaces and tabs until newline.
            # let's modify `res` to strip trailing spaces/tabs on the last element if it's on its own line
            # A simple way: check if res[-1] ends with newline + spaces
            last_str = res[-1] if res else ""
            
            # Check if all characters from the last newline in last_str are spaces/tabs
            last_newline = last_str.rfind('\n')
            if last_newline != -1:
                tail = last_str[last_newline+1:]
                if all(c in ' \t' for c in tail):
                    res[-1] = last_str[:last_newline+1] # remove the spaces
            elif all(c in ' \t' for c in last_str):
                res[-1] = ""

            i = curr
        else:
            # Semicolon not found where expected
            res.append(content[i:i+18])
            i += 18

    return "".join(res)

for root, dirs, files in os.walk("src"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r", encoding="utf-8") as f:
                content = f.read()
            new_content = remove_sout(content)
            if new_content != content:
                with open(path, "w", encoding="utf-8") as f:
                    f.write(new_content)
                print(f"Modified {path}")
