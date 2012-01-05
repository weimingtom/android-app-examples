#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"

#include "SimpleAudioEngine.h"

typedef enum {
	kDown =  0,
	kLeft = 1,
	kRight= 2,
	kUp = 3,
} HeroDirection;

typedef enum
{
	kNone = 1,//可以通行
	kWall,//墙
	kEnemy,//敌人
} CollisionType;//碰撞类型

using namespace cocos2d;

class HelloWorld : public cocos2d::CCLayer
{
public:
	HelloWorld();
	~HelloWorld();
    // Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
    virtual bool init();  

    // there's no 'id' in cpp, so we recommand to return the exactly class pointer
    static cocos2d::CCScene* scene();
    
    // a selector callback
    virtual void menuCloseCallback(CCObject* pSender);

    // implement the "static node()" method manually
    LAYER_NODE_FUNC(HelloWorld);

	CCTMXTiledMap *map;

	CCSprite* heroSprite;
	HeroDirection heroDirection;
	CCAnimation **walkAnimation;
	CCAnimation* createAnimationByDirection(HeroDirection direction);

	void menuCallBackMove(CCObject* pSender);

	void setFaceDirection(HeroDirection direction);
	void onWalkDone(CCNode* pTarget, void* data);

	CCPoint tileCoordForPosition(CCPoint position);
	CCPoint positionForTileCoord(CCPoint tileCoord);

	void setSceneScrollPosition(CCPoint position);
	bool isHeroWalking;
	void update(ccTime dt);
	CCPoint targetPosition;

	CollisionType checkCollision(CCPoint heroPosition);
};

#endif  // __HELLOWORLD_SCENE_H__