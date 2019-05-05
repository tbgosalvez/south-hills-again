# 
# use python 2.7
#

# for Brian's system (Anaconda install mixed up path)
#import sys
#sys.path.append('/home/tbg/anaconda2/lib/python2.7/site-packages')

import pygame
import random
import time
import datetime
import os

# classes
class Player(pygame.sprite.Sprite):
    def __init__(self, img='spaceship.png'):
        super(Player,self).__init__()
        self.isVisible = True
        
        self.sndCrash = pygame.mixer.Sound('crash.wav')
        
        self.vx = 0
        self.vy = 0
        self.image = pygame.image.load(img).convert()
        self.image.set_colorkey(Constants.color_white)
        self.rect = self.image.get_rect(center=
                                          (Constants.winWidth/2,Constants.winHeight-48))
    
    def update(self,win):
        if self.rect.x < 0 and self.vx < 0:
            self.vx = 0
        if self.rect.right > Constants.winWidth and self.vx > 0:
            self.vx = 0
        if self.rect.top < Constants.winHeight /2 and self.vy < 0:
            self.vy = 0
        if self.rect.bottom >= Constants.winHeight and self.vy > 0:
            self.vy = 0
            
        self.rect.x += self.vx
        self.rect.y += self.vy
        
        win.blit(self.image,self.rect)

      
class Enemy(pygame.sprite.Sprite):
    def __init__(self, img='asteroid.png'):
        super(Enemy,self).__init__()
        self.isVisible = False
        
        self.sndExp = pygame.mixer.Sound('exp.wav')
        self.sndExp.set_volume(0.3)
        
        self.theta = 0
        self.image = pygame.image.load(img)
     
        # uncomment if not rotating
        # self.image.set_colorkey(Constants.color_white)
        
        self.scale = random.randint(Constants.min_size_rock,Constants.max_size_rock)
        self.image = pygame.transform.scale(self.image, (self.scale,self.scale))
        self.rect = self.image.get_rect(center=(random.randint(0,Constants.winWidth),
                                                  random.randint(-64,0)))
    
    def update(self,win):
        self.rect.y += random.randint(1,6)
        self.theta += 6
        self.imageTrans = pygame.transform.rotate(self.image, self.theta)
        self.imageTrans.set_colorkey(Constants.color_white)
        
        win.blit(self.imageTrans,self.rect)
        
        
            
class Projectile(pygame.sprite.Sprite):
    def __init__(self, img, ship):
        super(Projectile,self).__init__()
        self.isVisible = False
        self.vx = 0
        self.vy = 0
        self.image = pygame.image.load(img)
        self.image.set_colorkey(Constants.color_white)
        self.rect = self.image.get_rect()
        self.rect.x, self.rect.y = ship.rect.x, ship.rect.y
        
        # audio
        self.sound = pygame.mixer.Sound('laser.wav')
        self.sound.set_volume(0.75)
        self.sound.play()
    
    
    def update(self,win):
        self.vy = -7
        self.rect.y += self.vy
        
        win.blit(self.image,self.rect)

    
class Star(pygame.sprite.Sprite):
    def __init__(self):
        super(Star,self).__init__()
        
        self.image = pygame.image.load('star.gif')
        self.randSize = random.randint(10,100)
        self.image = pygame.transform.scale(self.image,(self.randSize,self.randSize))
        self.image.set_colorkey(Constants.color_black)
        self.rect = self.image.get_rect(center=(random.uniform(0.0,Constants.winWidth),0))
        self.vy = random.randint(1,16)      
        
    def update(self,win):
        self.rect.y += self.vy

        win.blit(self.image,self.rect)
        
    
class Constants(object):
    winWidth = 640
    winHeight = 480
    winSize = (winWidth,winHeight)
    cFPS = 60
    color_black = (0,0,0)
    color_white = (255,255,255)
    color_blue = (50,50,127)
    color_yellow = (255,255,0)
    max_size_rock = 64
    min_size_rock = 5


# static
class Stats(object):
    hitCount = 0
    numLives = 9
    
# end classes



# init pygame
pygame.init()
pygame.mixer.init()



# globals

isRunning = True
clock = pygame.time.Clock()

# screen (window) handle
hwnd = pygame.display.set_mode(Constants.winSize)


# Sprites
spriteShip = Player('spaceship.png')
## random background image
list_bg = os.listdir('bg')
spriteBG = pygame.image.load(os.path.join('bg',list_bg[random.randint(0,len(list_bg)-1)])).convert()
spriteBG = pygame.transform.scale(spriteBG, (Constants.winWidth, Constants.winHeight))
## random font
sprFont = os.path.join('fonts', os.listdir('fonts')[random.randint(0,len(os.listdir('fonts'))-1)])

# Sprite Groups
sprGrpEnemies = pygame.sprite.Group()
sprGrpLasers = pygame.sprite.Group()
sprGrpStars = pygame.sprite.Group()
sprGrpAll = pygame.sprite.Group()
sprGrpAll.add(spriteShip)



# Events
evEnemySpawn = pygame.USEREVENT + 1
evStarSpawn = pygame.USEREVENT + 2
pygame.time.set_timer(evEnemySpawn, 500)
pygame.time.set_timer(evStarSpawn, 250)



# end globals


# functions

def initGame():
    
    # video
    pygame.display.set_caption('asteroids do not concern me, admiral.')
    
    # bgm
    list_bgm=[]
    for ff in os.listdir('bgm'):
        if ff.endswith('.ogg'):
            list_bgm.append(os.path.join('bgm',ff))

    pygame.mixer.music.load(list_bgm[random.randint(0,len(list_bgm)-1)])
    pygame.mixer.music.play(0,0.0)
    
    # objects
    # ...


def loadText(t, size):
    ff = pygame.font.Font(sprFont, size)
    fSurf = ff.render(t,True,Constants.color_yellow)
    
    return fSurf, fSurf.get_rect()


def dispMsg(t,s, c= (Constants.winWidth/2, Constants.winHeight/2)):
    Msg, MsgRect = loadText(t,s)
    MsgRect.center = c
    hwnd.blit(Msg,MsgRect)
    pygame.display.update()
    
    

def renderScene(window):
    global isRunning
    
    window.fill(Constants.color_black)
    hwnd.blit(spriteBG, (0,0))
    
    # render stars before other sprites
    for aa in sprGrpStars:
        aa.update(hwnd)
    
    for aa in sprGrpAll:
        aa.update(hwnd)

    # ship collide with an asteroid
    spAst = pygame.sprite.spritecollideany(spriteShip, sprGrpEnemies)
    if spAst:
        spriteShip.sndCrash.play(maxtime=2000)
        spriteShip.sndCrash.fadeout(2000)
        spAst.kill()
        Stats.numLives -= 1
        
    # too many collisions
    if Stats.numLives == 0:
        spriteShip.isVisible = False
        spriteShip.kill()
        print('game over!')
        dispMsg('game over',50)
        time.sleep(2)
        isRunning = False

    # lasers hit asteroid or go off screen
    for aa in sprGrpLasers: 
        spAst = pygame.sprite.spritecollideany(aa, sprGrpEnemies)
        if spAst:
            spAst.sndExp.play(maxtime=2000)            
            spAst.kill()
            aa.kill()
            Stats.hitCount += Constants.max_size_rock/spAst.scale
        if aa.rect.bottom < 0:
            aa.kill()
      

    tHits, tHitsRect = loadText('pwns:' + str(Stats.hitCount), 28)
    tLives,tLivesRect = loadText('goofs left:' + str(Stats.numLives), 28)
    tLivesRect.right = Constants.winWidth
    hwnd.blit(tHits,tHitsRect)
    hwnd.blit(tLives,tLivesRect)
    
    pygame.display.update()
    clock.tick(Constants.cFPS)



# end functions


# __main__

if __name__=='__main__':
    
    initGame()
    
    while isRunning:
        for ev in pygame.event.get():
            print ev
        
            if ev.type == pygame.KEYDOWN:
                if ev.key == pygame.K_q:
                    dispMsg('docking ship...', 24)
                    time.sleep(1)
                    isRunning = False
                    break
                
                if ev.key == pygame.K_LEFT:
                    spriteShip.vx = -5
                elif ev.key == pygame.K_RIGHT:
                    spriteShip.vx = 5
                if ev.key == pygame.K_UP:
                    spriteShip.vy = -5
                elif ev.key == pygame.K_DOWN:
                    spriteShip.vy = 5
                if ev.key == pygame.K_SPACE:
                   if spriteShip.isVisible:
                       spLaser = Projectile('laser.png',spriteShip)
                       sprGrpLasers.add(spLaser)
                       sprGrpAll.add(spLaser)
                        
            elif ev.type == pygame.KEYUP:
                if ev.key == pygame.K_LEFT or ev.key == pygame.K_RIGHT:
                    spriteShip.vx = 0
                elif ev.key == pygame.K_UP or ev.key == pygame.K_DOWN:
                    spriteShip.vy = 0
                    
            if ev.type == evEnemySpawn:
                spEnemy = Enemy()
                sprGrpEnemies.add(spEnemy)
                sprGrpAll.add(spEnemy)
                
            if ev.type == evStarSpawn:
                spStar = Star()
                sprGrpStars.add(spStar)

            print(spriteShip.rect.x,spriteShip.rect.y)
        # end for ev

        renderScene(hwnd)

        if pygame.mixer.music.get_busy() == False:
            isRunning = False
        
    # end while isRunning
    
    dispMsg('[go do something else now]', 24)
    time.sleep(3)
            
    pygame.mixer.music.stop()
    pygame.mixer.quit()
    pygame.quit()
    
    # record stats
    strNow = str(datetime.datetime.now())
    with open('stats.txt','a') as fOut:
        fOut.write(strNow + ': ' + str(Stats.hitCount) + ' hits!\n')
    
    quit()

# end __main__
