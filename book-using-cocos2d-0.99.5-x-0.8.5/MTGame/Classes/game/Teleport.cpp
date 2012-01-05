#include "Teleport.h"

Teleport::Teleport(CCStringToStringDictionary *dict, int x, int y)
{
	CCPoint position = ccp(x, y);
	//传送点所在的TileMap位置
	tileCoord = sGlobal->gameMap->tileCoordForPosition(ccp(x, y));
	//得出勇士在目标层的起始位置
	std::string key = "heroTileCoordX";
	int x1 = dict->objectForKey(key)->toInt();
	key = "heroTileCoordY";
	int y1 = dict->objectForKey(key)->toInt();
	heroTileCoord = ccp(x1, y1);
	//取得目标地图的层数
	key = "targetMap";
	targetMap = dict->objectForKey(key)->toInt();
	//获取image项
	key = "image";
	imagePath = dict->objectForKey(key);
	//创建用于显示Teleport的精灵
	teleportSprite = CCSprite::spriteWithFile(imagePath->m_sString.c_str());
	teleportSprite->setAnchorPoint(CCPointZero);
	teleportSprite->setPosition(position);
	sGlobal->gameLayer->addChild(teleportSprite, kZTeleport);
}

Teleport::~Teleport(void)
{
}