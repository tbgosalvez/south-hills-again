# 
# use python 2.7
#

# for Brian's system (Anaconda install mixed up path)
#import sys
#sys.path.append('/home/tbg/anaconda2/lib/python2.7/site-packages')

import pygame
import random

# classes
class Vertex(object):
    def __init__(self):
        self.x = 0
        self.y = 0
        
    def reset(self):
        self.x,self.y = 0,0
        
    def add(self, other):
        self.x += other.x
        self.y += other.y


class Sprite(object):
    def __init__(self, img, w=63,h=64):
        # get from img or stick with some standard
        self.width = w
        self.height = h
        
        # math
        self.vel = Vertex()
        self.pos = Vertex()
        
        # graphics
        self.surface = pygame.image.load(img)
        self.isVisible = False
        
    def setPos(self,x,y):
        self.pos.x, self.pos.y = x,y

        
    def updatePos(self,win,theta=0):
        self.pos.add(self.vel)     
        win.blit(self.surface,(self.pos.x,self.pos.y))
# wrap around screen        
#        if self.pos.x < 0-self.width:
 #           self.pos.x = Constants.winWidth
        if self.pos.y < 0-self.height:
            self.pos.y = Constants.winHeight
 #       if self.pos.x > Constants.winWidth:
  #          self.pos.x = 0
        if self.pos.y > Constants.winHeight:
            self.pos.y = 0

''' rotation
        orig_rect = self.surface.get_rect()
        rot_image = pygame.transform.rotate(self.surface, theta)
        rot_rect = orig_rect.copy()
        rot_rect.center = rot_image.get_rect().center
        rot_image = rot_image.subsurface(rot_rect).copy()
        self.surface = rot_image
''' 
class Constants(object):
    winWidth = 640
    winHeight = 480
    winSize = (winWidth,winHeight)
    cFPS = 60
    color_black = (0,0,0)
    color_white = (255,255,255)
    color_blue = (50,50,127)


# end classes


# globals

isRunning = True
clock = pygame.time.Clock()
hwnd = pygame.display.set_mode(Constants.winSize)
spriteShip = Sprite('spaceship.png',34,31)
spriteAsteroid = Sprite('asteroid.png',128,128)
spriteLaser = Sprite('laser.png')

# end globals


# functions

def initGame(window):
    pygame.display.set_caption('retro space shooter')

    spriteShip.pos.x, spriteShip.pos.y = Constants.winWidth*0.40,Constants.winHeight*0.8
    spriteShip.isVisible = True
    
    spriteAsteroid.pos.x, spriteAsteroid.pos.y = random.randint(0,Constants.winWidth-spriteAsteroid.width), random.randint(0, Constants.winHeight/2)
    spriteAsteroid.vel.x, spriteAsteroid.vel.y = random.randint(0,3), random.randint(0, 3)
    spriteAsteroid.isVisible = True
    
    spriteLaser.setPos(spriteShip.pos.x,spriteShip.pos.y)


def renderScene(window):
    window.fill(Constants.color_black)

    if spriteShip.pos.x < 0 and spriteShip.vel.x < 0:
        spriteShip.vel.x = 0
    if spriteShip.pos.x > Constants.winWidth-spriteShip.width and spriteShip.vel.x > 0:
        spriteShip.vel.x = 0
        
    spriteShip.updatePos(window)
    spriteAsteroid.updatePos(window,theta=1)

    # laser bullet
    if spriteLaser.isVisible == True:
        spriteLaser.vel.y = -15
        spriteLaser.updatePos(window)
        if spriteLaser.pos.y < 0:
            spriteLaser.isVisible = False
            spriteLaser.vel.y = 0
    else:
        spriteLaser.setPos(spriteShip.pos.x,spriteShip.pos.y)


    
    pygame.display.update()
    clock.tick(Constants.cFPS)

# end functions


# __main__

if __name__=='__main__':
    
    initGame(hwnd)
    
    while isRunning:
        for ev in pygame.event.get():
            print ev
        
            if ev.type == pygame.KEYDOWN:
                if ev.key == pygame.K_q:
                    isRunning = False
                    break
                
                if ev.key == pygame.K_LEFT:
                    spriteShip.vel.x = -5
                elif ev.key == pygame.K_RIGHT:
                    spriteShip.vel.x = 5
                if ev.key == pygame.K_UP:
                    spriteShip.vel.y = -5
                elif ev.key == pygame.K_DOWN:
                    spriteShip.vel.y = 5
                if ev.key == pygame.K_SPACE:
                    if spriteLaser.isVisible == False:
                        spriteLaser.isVisible = True
                        
            elif ev.type == pygame.KEYUP:
                if ev.key == pygame.K_LEFT or ev.key == pygame.K_RIGHT:
                    spriteShip.vel.x = 0
                elif ev.key == pygame.K_UP or ev.key == pygame.K_DOWN:
                    spriteShip.vel.y = 0
                    
            print(spriteShip.pos.x,spriteShip.pos.y)

        renderScene(hwnd)

    pygame.quit()
    quit()

# end __main__
