#include "AnimationManager.h"

DECLARE_SINGLETON_MEMBER(AnimationManager);

AnimationManager::AnimationManager()
{
}

AnimationManager::~AnimationManager()
{
	//CCDirector���Լ����AnimationCache
	//CCAnimationCache::purgeSharedAnimationCache();
}

bool AnimationManager::initAnimationMap()
{
	char temp[20];
	sprintf(temp, "%d", aDown);
	//������ʿ�����ߵĶ���
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kDown), temp);
	sprintf(temp, "%d", aRight);
	//������ʿ�����ߵĶ���
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kRight), temp);
	sprintf(temp, "%d", aLeft);
	//������ʿ�����ߵĶ���
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kLeft), temp);
	sprintf(temp, "%d", aUp);
	//������ʿ�����ߵĶ���
	CCAnimationCache::sharedAnimationCache()->addAnimation(createHeroMovingAnimationByDirection(kUp), temp);
	//����ս������
	sprintf(temp, "%d", aFight);
	CCAnimationCache::sharedAnimationCache()->addAnimation(createFightAnimation(), temp);
	//����NPC����
	CCAnimationCache::sharedAnimationCache()->addAnimation(createNPCAnimation(), "npc0");
	return true;
}

CCAnimation* AnimationManager::createHeroMovingAnimationByDirection(HeroDirection direction)
{
	CCTexture2D *heroTexture = CCTextureCache::sharedTextureCache()->addImage("hero.png");
	CCSpriteFrame *frame0, *frame1, *frame2, *frame3;
	//�ڶ���������ʾ��ʾ�����x, y, width, height������direction��ȷ����ʾ��y����
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
	//0.05f��ʾÿ֡������ļ��
	animation->initWithFrames(animFrames, 0.05f);
	animFrames->release();

	return animation;
}

//����ս������ģ��
CCAnimation* AnimationManager::createFightAnimation()
{
	//����ÿ֡�����
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
		//����ÿ֡�����������е�ƫ����
		x = fightAnim[i] % 5 - 1;
		y = fightAnim[i] / 5;
		frame = CCSpriteFrame::frameWithTexture(texture, cocos2d::CCRectMake(192*x, 192*y, 192, 192));
		//��17��19֡��y��������-8��ƫ��
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
	//�ڶ���������ʾ��ʾ�����x, y, width, height������direction��ȷ����ʾ��y����
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
	//0.05f��ʾÿ֡������ļ��
	animation->initWithFrames(animFrames, 0.2f);
	animFrames->release();

	return animation;
}

//��ȡָ������ģ��
CCAnimation* AnimationManager::getAnimation(int key)
{
	char temp[20];
	sprintf(temp, "%d", key);
	return CCAnimationCache::sharedAnimationCache()->animationByName(temp);
}

//��ȡһ��ָ������ģ���ʵ��
CCAnimate* AnimationManager::createAnimate(int key)
{
	//��ȡָ������ģ��
	CCAnimation* anim = getAnimation(key);
	if(anim)
	{
		//���ݶ���ģ������һ������ʵ��
		return cocos2d::CCAnimate::actionWithAnimation(anim);
	}
	return NULL;
}

//��ȡһ��ָ������ģ���ʵ��
CCAnimate* AnimationManager::createAnimate(const char* key)
{
	//��ȡָ������ģ��
	CCAnimation* anim = CCAnimationCache::sharedAnimationCache()->animationByName(key);
	if(anim)
	{
		//���ݶ���ģ������һ������ʵ��
		return cocos2d::CCAnimate::actionWithAnimation(anim);
	}
	return NULL;
}