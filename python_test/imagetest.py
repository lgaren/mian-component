from PIL import Image
import pytesseract

pip install pytesseract tesseract
yum install tesseract

path = "/root/Downloads/8def2a9d-6b5c-4ef9-9fd2-c832ee9e511c.jpg"
text=pytesseract.image_to_string(Image.open(path),lang='chi_sim') #Ê¶±ðÎÄ×Ö
print(text)
