import sys 
import json

result = {
    'a': 1
  }

json = json.dumps(result)

print(str(json))
sys.stdout.flush()