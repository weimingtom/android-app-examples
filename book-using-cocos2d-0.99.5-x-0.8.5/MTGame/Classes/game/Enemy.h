#ifndef _ENEMY_H_
#define _ENEMY_H_

#include "cocos2d.h"

using namespace cocos2d;

class Enemy : public CCObject
{
public:
	Enemy(void);
	~Enemy(void);
	//������TileMap�ϵķ�λ
	CCPoint position;
	//�����ʼ��ͼ��ID
	int startGID;
	//�������ֵ��е�index
	int index;
};

#endif