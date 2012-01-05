#ifndef __TELEPORT_H__
#define __TELEPORT_H__

#include "MTGame.h"

using namespace cocos2d;

class Teleport : public CCObject
{
public:
	Teleport(CCStringToStringDictionary *dict, int x, int y);
	~Teleport(void);
	//���͵�����λ��
	CCPoint tileCoord;
	//���͵�Ŀ������ʿ��������
	CCPoint heroTileCoord;
	//Ŀ���ͼ�Ĳ���
	int targetMap;
	//Ψһ��ID
	int index;
	//ͼƬ������ļ�·��
	CCString *imagePath;
	CCSprite *teleportSprite;
};
#endif