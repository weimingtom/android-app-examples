#ifndef __GAME_MAP_H__
#define __GAME_MAP_H__

#include "MTGame.h"

using namespace cocos2d;

class NPC;
class Teleport;

//�̳���CCTMXTIledMap
class GameMap : public cocos2d::CCTMXTiledMap
{
	//ֻ����������ȡ��ͼ��
	CC_PROPERTY_READONLY(CCTMXLayer*, floorLayer, FloorLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, wallLayer, WallLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, enemyLayer, EnemyLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, itemLayer, ItemLayer);
	CC_PROPERTY_READONLY(CCTMXLayer*, doorLayer, DoorLayer);
public:
	GameMap(void);
	~GameMap(void);
	//��̬��������������GameMapʵ��
	static GameMap* gameMapWithTMXFile(const char *tmxFile);
	//TiledMap��cocos2d-x����ϵ�໥ת���ķ���
	CCPoint tileCoordForPosition(CCPoint position);
	CCPoint positionForTileCoord(CCPoint tileCoord);
	void showEnemyHitEffect(CCPoint tileCoord);
	//��ŵ�ͼ�Ϲ���������Լ�npc 
	CCMutableArray<Enemy*> *enemyArray;
	CCMutableDictionary<int, Teleport*> *teleportDict;
	CCMutableDictionary<int, NPC*> *npcDict;
protected:
	//TiledMap����ĳ�ʼ������
	void extraInit();
	//��ʼ����������
	void initEnemy();
	//��ʼ������
	void initObject();
	//������ͼ����������
	void enableAnitiAliasForEachLayer();
	//���¹��ﶯ��
	void updateEnemyAnimation(ccTime dt);
	//��ʱ����ս��ʱ�Ĺ���
	CCSprite* fightingEnemy;
	//��ʱ����������
	int fightCount;
	//���¹���ս��ʱ����ɫ״̬
	void updateEnemyHitEffect(ccTime dt);
};

#endif