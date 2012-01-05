#ifndef __NPC_H__
#define __NPC_H__

#include "MTGame.h"

using namespace cocos2d;

class NPC : public CCObject
{
public:
	//���캯����Ҫ���ݴ��ݵ����Ա��ʼ����������
	NPC(CCStringToStringDictionary *dict, int x, int y);
	~NPC(void);
	//������ʾnpc�ľ���
	CCSprite *npcSprite;
	//������TileMap�����õ�name��
	CCString *npcId;
	//npc���ڵ�TileMap����
	CCPoint tileCoord;
	//ͼƬ������ļ�·��
	CCString *imagePath;
	//�����Rect
	CCRect rect;
	//��Ӧ�����е�type��
	CCString *type;
};

#endif