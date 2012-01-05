#ifndef __GAME_CONSTANTS_H__
#define __GAME_CONSTANTS_H__

typedef enum {
	kDown =  0,//向下方向
	kLeft = 1,//向左方向
	kRight= 2,//向右方向
	kUp = 3,//向上方向
	kNormal,
} HeroDirection;//勇士方向

typedef enum
{
	kNone = 1,//可以通行
	kWall,//墙
	kEnemy,//敌人
	kItem,//物品
	kDoor,//门
	kNPC,//npc
	kTeleport,//传送点
} CollisionType;//碰撞类型

typedef enum
{
	aDown = 0,//向下行走动画
	aLeft,//向左行走动画
	aRight,//向右行走动画
	aUp,//向上行走动画
	aFight,//刀光动画
} AnimationKey;//动画模版键值

enum 
{
	kZMap = 0,//地图的zOrder
	kZNPC,
	kZTeleport,
	kZHero,//勇士精灵的zOrder
	kZTip,//提示信息的zOrder
};//GameLayer中各部分的显示zOrder及tag

enum 
{
	kGameLayer = 0,
	kControlLayer,
	kFadeLayer,
};

#endif