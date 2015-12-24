

#ifdef BULLETTRY_STATIC
#include <btBulletDynamicsCommon.h>
#include <memory>
#elif BULLETTRY_SINGLE
#define PALBULLET282_CPP
#include "palBullet282.hpp"
#else
#error This needs to be built as static or single
#endif

#include <iostream>
#include <assert.h>

template<typename T>
class ref_ptr
{
	struct block
	{
		T* _pointer;
		uint32_t _count;
	};

	block* _block;

public:
	ref_ptr(void)
	{
		_block = nullptr;
	}

	ref_ptr(T* pointer)
	{
		if (!pointer)
		{
			_block = nullptr;
		}
		else
		{
			_block = new block;
			_block->_pointer = pointer;
			_block->_count = 1;
		}
	}

	ref_ptr& operator=(T*) = delete;

	ref_ptr(const ref_ptr<T>&) = delete;
	ref_ptr& operator=(const ref_ptr<T>&) = delete;

	ref_ptr(ref_ptr<T>& rp)
	{
		if (rp._block)
		{
			++((_block = rp._block)->_count);
		}
		else
		{
			_block = nullptr;
		}
	}

	~ref_ptr(void)
	{
		if (!_block)
		{
			return;
		}

		--(_block->_count);

		if (_block->_count)
		{
			return;
		}

		delete _block->_pointer;
		delete _block;

		_block = nullptr;
	}

	T* operator ->(void)
	{
		assert(_block);
		assert(_block->_pointer);
		return _block->_pointer;
	}

	operator T* (void)
	{
		return _block->_pointer;
	}

	operator const T* (void) const
	{
		return _block->_pointer;
	}
};

int main(int argc, char* argv[])
{
	std::cout << "Hello World" << std::endl;

	{
		// Build the broadphase
		ref_ptr<btBroadphaseInterface> broadphase = new btDbvtBroadphase();

		// Set up the collision configuration and dispatcher
		ref_ptr<btDefaultCollisionConfiguration> collisionConfiguration = new btDefaultCollisionConfiguration();
		ref_ptr<btCollisionDispatcher> dispatcher = new btCollisionDispatcher(collisionConfiguration);

		// The actual physics solver
		ref_ptr<btSequentialImpulseConstraintSolver> solver = new btSequentialImpulseConstraintSolver;

		// The world.
		ref_ptr<btDiscreteDynamicsWorld> dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		dynamicsWorld->setGravity(btVector3(0, -10, 0));
		
		// setup the ground
		ref_ptr<btCollisionShape> groundShape = new btStaticPlaneShape(btVector3(0, 1, 0), 1);
		ref_ptr<btDefaultMotionState> groundMotionState = new btDefaultMotionState(btTransform(btQuaternion(0, 0, 0, 1), btVector3(0, -1, 0)));
		btRigidBody::btRigidBodyConstructionInfo groundRigidBodyCI(0, groundMotionState, groundShape, btVector3(0, 0, 0));
		ref_ptr<btRigidBody> groundRigidBody = new btRigidBody(groundRigidBodyCI);
		dynamicsWorld->addRigidBody(groundRigidBody);

		// setup the ball
		ref_ptr<btCollisionShape> fallShape = new btSphereShape(1);
		ref_ptr<btDefaultMotionState> fallMotionState = new btDefaultMotionState(btTransform(btQuaternion(0, 0, 0, 1), btVector3(0, 50, 0)));
		btScalar mass = 1;
		btVector3 fallInertia(0, 0, 0);
		fallShape->calculateLocalInertia(mass, fallInertia);
		btRigidBody::btRigidBodyConstructionInfo fallRigidBodyCI(mass, fallMotionState, fallShape, fallInertia);
		ref_ptr<btRigidBody> fallRigidBody = new btRigidBody(fallRigidBodyCI);
		dynamicsWorld->addRigidBody(fallRigidBody);

		// Stepping The Simulation
		for (int i = 0; i < 300; i++)
		{
			dynamicsWorld->stepSimulation(1 / 60.f, 10);

			btTransform trans;
			fallRigidBody->getMotionState()->getWorldTransform(trans);

			std::cout << "sphere height: " << trans.getOrigin().getY() << std::endl;
		}

		// Clean up behind ourselves like good little programmers		
		dynamicsWorld->removeRigidBody(fallRigidBody);
		dynamicsWorld->removeRigidBody(groundRigidBody);

		// ... and ref_ptr<...> does the rest
	}

	return EXIT_SUCCESS;
}
