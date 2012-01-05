#ifndef __GAME_MAP_H__
#define __GAME_MAP_H__

#include "MTGame.h"

using namespace cocos2d;

class NPC;
class Teleport;

//继承自CCTMXTIledMap
class GameMap : public cocos2d::CCTMXTiledMap
{
	//只读变量，获取各图层
	CC_PROPERTY_READONLY(CCTMXLayer*, floorLayer, FloorLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, wallLayer, WallLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, enemyLayer, EnemyLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, itemLayer, ItemLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, doorLayer, DoorLayer);
public:
	GameMap(void);
	~GameMap(void);
	//静态方法，用于生成GameMap实例
	static GameMap* gameMapWithTMXFile(const char *tmxFile);
	//TiledMap和cocos2d-x坐标系相互转换的方法
	CCPoint tileCoordForPosition(CCPoint position);
	CCPoint positionForTileCoord(CCPoint tileCoord);
	void showEnemyHitEffect(CCPoint tileCoord);
	//存放地图上怪物、传送门以及npc 
	CCMutableArray<Enemy*> *enemyArray;
	CCMutableDictionary<int, Teleport*> *teleportDict;
	CCMutableDictionary<int, NPC*> *npcDict;
protected:
	//TiledMap额外的初始化方法
	void extraInit();
	//初始化怪物数组
	void initEnemy();
	//初始化对象
	void initObject();
	//开启各图层的纹理抗锯齿
	void enableAnitiAliasForEachLayer();
	//更新怪物动画
	void updateEnemyAnimation(ccTime dt);
	//临时保存战斗时的怪物
	CCSprite* fightingEnemy;
	//临时保存打击次数
	int fightCount;
	//更新怪物战斗时的颜色状态
	void updateEnemyHitEffect(ccTime dt);
};

#endif