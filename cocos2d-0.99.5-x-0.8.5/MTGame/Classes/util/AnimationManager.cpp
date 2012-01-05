#include "AnimationManager.h"

DECLARE_SINGLETON_MEMBER(AnimationManager);

AnimationManager::AnimationManager()
{
}

AnimationManager::~AnimationManager()
{
	//CCDirector会自己清除AnimationCache
	//CCAnimationCache::purgeSharedAnimationCache();
}

bool AnimationManager::initAnimationMap()
{
	char temp[20];
	sprintf(temp, "%d", aDown);
	//加载勇士向下走的动画
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kDown), temp);
	sprintf(temp, "%d", aRight);
	//加载勇士向右走的动画
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kRight), temp);
	sprintf(temp, "%d", aLeft);
	//加载勇士向左走的动画
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kLeft), temp);
	sprintf(temp, "%d", aUp);
	//加载勇士向上走的动画
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kUp), temp);
	//加载战斗动画
	sprintf(temp, "%d", aFight);
	CCAnimationCache::sharedAnimationCache()->addAnimation(createFightAnimation(), temp);
	//加载NPC动画
	CCAnimationCache::sharedAnimationCache()->addAnimation(createNPCAnimation(), "npc0");
	return true;
}

CCAnimation* AnimationManager::createHeroMovingAnimationByDirection(HeroDirection direction)
{
	CCTexture2D *heroTexture = CCTextureCache::sharedTextureCache()->addImage("hero.png");
	CCSpriteFrame *frame0, *frame1, *frame2, *frame3;
	//第二个参数表示显示区域的x, y, width, height，根据direction来确定显示的y坐标
	frame0 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*0, 32*direction, 32, 32));
	frame1 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*1, 32*direction, 32, 32));
	frame2 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*2, 32*direction, 32, 32));
	frame3 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*3, 32*direction, 32, 32));
	CCMutableArray<CCSpriteFrame*>* animFrames = new CCMutableArray<CCSpriteFrame*>(4);
	animFrames->addObject(frame0);
	animFrames->addObject(frame1);
	animFrames->addObject(frame2);
	animFrames->addObject(frame3);
	CCAnimation* animation = new CCAnimation();
	//0.05f表示每帧动画间的间隔
	animation->initWithFrames(animFrames, 0.05f);
	animFrames->release();

	return animation;
}

//创建战斗动画模板
CCAnimation* AnimationManager::createFightAnimation()
{
	//定义每帧的序号
	int fightAnim[] = 
	{
		4,6,8,10,13,15,17,19,20,22
	};
	CCMutableArray<CCSpriteFrame*>* animFrames = new CCMutableArray<CCSpriteFrame*>();
	CCTexture2D *texture = CCTextureCache::sharedTextureCache()->addImage("sword.png");
	CCSpriteFrame *frame;
	int x, y;
	for (int i = 0; i < 10; i++) 
	{
		//计算每帧在整个纹理中的偏移量
		x = fightAnim[i] % 5 - 1;
		y = fightAnim[i] / 5;
		frame = CCSpriteFrame::frameWithTexture(texture, cocos2d::CCRectMake(192*x, 192*y, 192, 192));
		//第17和19帧在y方向上有-8的偏移
		if (fightAnim[i] == 17 || fightAnim[i] == 19)
		{
			frame->setOffsetInPixels( ccp(0, -8) );
		}
		animFrames->addObject(frame);
	}
	CCAnimation* animation = new CCAnimation();
	animation->initWithFrames(animFrames, 0.1f);
	animFrames->release();
	return animation;
}

CCAnimation* AnimationManager::createNPCAnimation()
{
	CCTexture2D *heroTexture = CCTextureCache::sharedTextureCache()->addImage("npc.png");
	CCSpriteFrame *frame0, *frame1, *frame2, *frame3;
	//第二个参数表示显示区域的x, y, width, height，根据direction来确定显示的y坐标
	frame0 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*0, 0, 32, 32));
	frame1 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*1, 0, 32, 32));
	frame2 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*2, 0, 32, 32));
	frame3 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*3, 0, 32, 32));
	CCMutableArray<CCSpriteFrame*>* animFrames = new CCMutableArray<CCSpriteFrame*>(4);
	animFrames->addObject(frame0);
	animFrames->addObject(frame1);
	animFrames->addObject(frame2);
	animFrames->addObject(frame3);
	CCAnimation* animation = new CCAnimation();
	//0.05f表示每帧动画间的间隔
	animation->initWithFrames(animFrames, 0.2f);
	animFrames->release();

	return animation;
}

//获取指定动画模版
CCAnimation* AnimationManager::getAnimation(int key)
{
	char temp[20];
	sprintf(temp, "%d", key);
	return CCAnimationCache::sharedAnimationCache()->animationByName(temp);
}

//获取一个指定动画模版的实例
CCAnimate* AnimationManager::createAnimate(int key)
{
	//获取指定动画模版
	CCAnimation* anim = getAnimation(key);
	if(anim)
	{
		//根据动画模版生成一个动画实例
		return cocos2d::CCAnimate::actionWithAnimation(anim);
	}
	return NULL;
}

//获取一个指定动画模版的实例
CCAnimate* AnimationManager::createAnimate(const char* key)
{
	//获取指定动画模版
	CCAnimation* anim = CCAnimationCache::sharedAnimationCache()->animationByName(key);
	if(anim)
	{
		//根据动画模版生成一个动画实例
		return cocos2d::CCAnimate::actionWithAnimation(anim);
	}
	return NULL;
}