#ifndef _ANIMATION_MANAGER_H_
#define _ANIMATION_MANAGER_H_

#include "MTGame.h"

using namespace cocos2d;

class AnimationManager : public Singleton<AnimationManager>
{
public:
	AnimationManager();
	~AnimationManager();
	//��ʼ������ģ�滺���
	bool initAnimationMap();
	//�������ֵõ�һ������ģ��
	CCAnimation* getAnimation(int key);
	//����һ������ʵ��
	CCAnimate* createAnimate(int key);
	//����һ������ʵ��
	CCAnimate* createAnimate(const char* key);
protected:
	//������ʿ���߶���ģ��
	CCAnimation* createHeroMovingAnimationByDirection(HeroDirection direction);
	CCAnimation* createFightAnimation();
	CCAnimation* createNPCAnimation();
};
//���嶯��������ʵ���ı���
#define sAnimationMgr AnimationManager::instance()

#endif