package su.wps.modelfactory.fields

import su.wps.modelfactory.reflection.ObjectReflector

import scala.reflect.ClassTag

/**
  * This class works together with the ObjectBuilder to let users create some
  * FieldSetter for a certain field in a certain object type.
  *
  * It has 2 abstract types:
  * ObjectType is the type of the object to build
  * FieldType is the type of the field to set
  *
  * This will ensure that everything is type safe
  *
  */
class FieldBuilder(val propName: String) {

  type objectType
  type fieldType

  def mapsTo(value : fieldType)(implicit tag : ClassTag[fieldType]) : FieldSetter[objectType, fieldType] = {
    new SpecifiedFieldSetter[objectType, fieldType](propName, value, fieldClass)
  }

  def isRandom(implicit random : Randomizer[fieldType], tag : ClassTag[fieldType]) : FieldSetter[objectType, fieldType] =
    mapsTo(random())

  def isRandom(random : (() => fieldType)) (implicit tag : ClassTag[fieldType]) : FieldSetter[objectType, fieldType] =
    mapsTo(random())

  def isAnotherFactoryModel(implicit tag : ClassTag[fieldType]) =
    new OtherModelFieldSetter[objectType, fieldType](propName, fieldClass)

  private def fieldClass(implicit tag : ClassTag[fieldType]) = ObjectReflector.clazz[fieldType]

}
