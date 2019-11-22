
import turtle
import datetime

#ÉúÈÕ¿ìÀÖ
#PythonÑ§Ï°Èº548377875
def love():
    def func(x, y):
        main()
    turtle.title('Áìµ¼×¨ÓÃ³ÌÐò')
    lv=turtle.Turtle()
    lv.hideturtle()
    lv.getscreen().bgcolor('light blue')
    lv.color('yellow','red')
    lv.pensize(1)
    lv.speed(1)
    lv.up()
    lv.goto(0,-150)
    #¿ªÊ¼»­°®ÐÄ
    lv.down()
    lv.begin_fill()
    lv.goto(0, -150)
    lv.goto(-175.12, -8.59)
    lv.left(140)
    pos = []
    for i in range(19):
        lv.right(10)
        lv.forward(20)
        pos.append((-lv.pos()[0], lv.pos()[1]))
    for item in pos[::-1]:
        lv.goto(item)
    lv.goto(175.12, -8.59)
    lv.goto(0, -150)
    lv.left(50)
    lv.end_fill()
    #Ð´×Ö
    lv.up()
    lv.goto(0, 80)
    lv.down()
    lv.write("ß÷Áìµ¼",font=(u"·½ÕýÊæÌå",36,"normal"),align="center")
    lv.up()
    lv.goto(0, 0)
    lv.down()
    lv.write("ÉúÈÕ¿ìÀÖ£¡",font=(u"·½ÕýÊæÌå",48,"normal"),align="center")
    lv.up()
    lv.goto(100, -210)
    lv.down()
    lv.write("µãÎÒµãÎÒ¿ìµãÎÒ",font=(u"»ªÎÄçúçê",26,"bold"),align="right")
    lv.up()
    lv.goto(160, -190)
    lv.resizemode('user')
    lv.shapesize(4, 4, 10)#µ÷ÕûÐ¡ÎÚ¹ê´óÐ¡£¬ÒÔ±ã¸²¸Ç¡°µãÎÒ¡±ÎÄ×Ö
    lv.color('red', 'red')
    lv.onclick(func)
    lv.showturtle()


def main():
    pass

if __name__ == '__main__':
    if datetime.date.today() == datetime.date('YYYY', 'MM', 'DD'): #YYYYÄê,MMÔÂ,DDÈÕ
        love()
    else:
        main()