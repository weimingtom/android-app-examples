#ifndef __HERO_H__
#define __HERO_H__

#include "MTGame.h"

using namespace cocos2d;

class Teleport;

//勇士类继承自CCNode
class Hero : public cocos2d::CCNode
{
public:
	Hero(void);
	~Hero(void);
	//静态方法，用于创建勇士实例
	static Hero *heroWithinLayer();
	//让勇士向指定方向移动一格
	void move(HeroDirection direction);
	//设置勇士朝向
	void setFaceDirection(HeroDirection direction);
	//开始战斗逻辑
	void fight();
	//标识勇士是否在移动状态
	bool isHeroMoving;
	//标识勇士是否在战斗状态
	bool isHeroFighting;
	//标识勇士是否在开门状态
	bool isDoorOpening;
	//拾取物品
	void pickUpItem();
	//开门
	void openDoor(int targetDoorGID);
	//对NPC交互
	void actWithNPC();
	//传送
	void doTeleport(Teleport *teleport);
protected:
	//用于显示勇士形象的精灵
	CCSprite *heroSprite;
	//临时保存目标的Tilemap坐标
	CCPoint targetTileCoord;
	//临时保存目标的cocos2d-x坐标
	CCPoint targetPosition;
	//临时保存门起始的图块ID
	int targetDoorGID;
	//显示战斗动画的精灵
	CCSprite *fightSprite;
	//初始化方法
	bool heroInit();
	//战斗完成后的回调函数
	void onFightDone(CCNode* pTarget);
	//碰撞检测方法
	CollisionType checkCollision(CCPoint heroPosition);
	//移动完成后的回调函数
	void onMoveDone(CCNode* pTarget, void* data);
	//更新开门动画
	void updateOpenDoorAnimation(ccTime dt);
};

#endif