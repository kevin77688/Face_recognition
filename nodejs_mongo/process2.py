import sys 
import json
from PIL import Image

array = sys.argv[1].split(',')
jsonParsed = json.dumps(array)

for _ in range (1, len(array), 2):
    img = Image.open('uploads/' + array[_])
    img.show()

print(jsonParsed)
# print(str(json))
sys.stdout.flush()