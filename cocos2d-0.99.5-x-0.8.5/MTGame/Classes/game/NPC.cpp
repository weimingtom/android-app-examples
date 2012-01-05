#include "NPC.h"

NPC::NPC(CCStringToStringDictionary *dict, int x, int y)
{
	//��ȡ����
	std::string key = "name";
	npcId = dict->objectForKey(key);
	//��ȡ����
	key = "type";
	type = dict->objectForKey(key);
	//��ȡimage��
	key = "image";
	imagePath = dict->objectForKey(key);
	//��ȡrectX��rectY
	key = "rectX";
	int x1 = dict->objectForKey(key)->toInt();
	key = "rectY";
	int y1 = dict->objectForKey(key)->toInt();
	rect = CCRectMake(x1, y1, 32, 32);
	//positionΪcocos2d-x���꣬tileCoordΪTileMap����
	CCPoint position = ccp(x, y);
	tileCoord = sGlobal->gameMap->tileCoordForPosition(position);
	//����������ʾnpc�ľ���
	npcSprite = CCSprite::spriteWithFile(imagePath->m_sString.c_str(), rect);
	npcSprite->setAnchorPoint(CCPointZero);
	npcSprite->setPosition(position);
	sGlobal->gameLayer->addChild(npcSprite, kZNPC);
	//�Ӷ����������и���npcId��ȡ��������ʼ���ò���
	CCAnimate* animation = sAnimationMgr->createAnimate(npcId->m_sString.c_str());
	if (animation != NULL) {
		CCActionInterval* action = CCRepeatForever::actionWithAction(animation);
		npcSprite->runAction(action);
	}
}

NPC::~NPC(void)
{
	//�ͷ�CCString����
	CC_SAFE_RELEASE(npcId)
	CC_SAFE_RELEASE(imagePath)
	CC_SAFE_RELEASE(type)
}
