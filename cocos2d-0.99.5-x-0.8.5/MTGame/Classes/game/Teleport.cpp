#include "Teleport.h"

Teleport::Teleport(CCStringToStringDictionary *dict, int x, int y)
{
	CCPoint position = ccp(x, y);
	//���͵����ڵ�TileMapλ��
	tileCoord = sGlobal->gameMap->tileCoordForPosition(ccp(x, y));
	//�ó���ʿ��Ŀ������ʼλ��
	std::string key = "heroTileCoordX";
	int x1 = dict->objectForKey(key)->toInt();
	key = "heroTileCoordY";
	int y1 = dict->objectForKey(key)->toInt();
	heroTileCoord = ccp(x1, y1);
	//ȡ��Ŀ���ͼ�Ĳ���
	key = "targetMap";
	targetMap = dict->objectForKey(key)->toInt();
	//��ȡimage��
	key = "image";
	imagePath = dict->objectForKey(key);
	//����������ʾTeleport�ľ���
	teleportSprite = CCSprite::spriteWithFile(imagePath->m_sString.c_str());
	teleportSprite->setAnchorPoint(CCPointZero);
	teleportSprite->setPosition(position);
	sGlobal->gameLayer->addChild(teleportSprite, kZTeleport);
}

Teleport::~Teleport(void)
{
}