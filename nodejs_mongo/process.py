import sys 
import json

result = {
    'sum': int(sys.argv[1]) + int(sys.argv[2])
  }

json = json.dumps(result)

print(str(json))
sys.stdout.flush()